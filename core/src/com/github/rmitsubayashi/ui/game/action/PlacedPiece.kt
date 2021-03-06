package com.github.rmitsubayashi.ui.game.action

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.AnimationConfig
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIBoard
import com.github.rmitsubayashi.ui.game.UIHand
import com.github.rmitsubayashi.ui.game.UIPiecePool

class PlacedPiece(
                  private val uiBoard: UIBoard,
                  private val uiHand: UIHand,
                  private val uiPiecePool: UIPiecePool
)
    : Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.placePiece) return false
        if (event.data?.get(EventDataKey.DONE) != true) return false
        if (event.data[EventDataKey.SQUARE] !is Int) return false
        if (event.actor !is Piece) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val piece = event.actor as Piece
        val moved = uiBoard.findPiece(piece) != null
        val uiPiece = if (moved) {
            // after a move ability
            uiBoard.removePiece(piece)
        }else if (piece.isToken()) {
            // spawn token ability
            uiPiecePool.createUIPiece(piece)
        }else if (piece.player == game.player1) {
            // should take from the player's pieces (actual player)
            uiHand.removePiece(piece)
        } else {
            //should take from the piece pool (opponent player)
             uiPiecePool.createUIPiece(piece)
        }
        uiPiece ?: return emptyList()
        val square = event.data?.get(EventDataKey.SQUARE) as Int
        val isAI = event.data[EventDataKey.IS_USER_EVENT] == false
        if (isAI || piece.isToken() || moved) {
            // for ai and abilities, want to place pieces one by one
            game.animationQueue.addAnimation(
                    AnimationConfig(
                            Actions.delay(1f),
                            null,
                            1f
                    ) {
                        uiBoard.placePiece(uiPiece, square)
                    }
            )
        } else {
            uiBoard.placePiece(uiPiece, square)
        }
        return if ((piece.isToken()||moved) && !isAI) {
            //we want the animation right now if it's a user action
            return listOf(
                    Event(EventType.shouldAnimate, null, null)
            )
        } else {
            emptyList()
        }
    }

    override fun copy(): Action {
        return PlacedPiece(uiBoard, uiHand, uiPiecePool)
    }
}