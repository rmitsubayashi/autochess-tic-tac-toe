package com.github.rmitsubayashi.ui.game.action

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.AnimationConfig
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIBoard
import com.github.rmitsubayashi.ui.game.UIPiecePool
import com.github.rmitsubayashi.ui.game.UIHand

class PlacedPiece(eventActor: EventActor,
                  private val uiBoard: UIBoard,
                  private val uiHand: UIHand,
                  private val uiPiecePool: UIPiecePool
)
    : Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.placePiece) return false
        if (event.data?.get(EventDataKey.DONE) != true) return false
        if (event.data[EventDataKey.SQUARE] !is Int) return false
        if (event.actor !is Piece) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val piece = event.actor as Piece
        val uiPiece = if (piece.player == game.player1) {
            // should take from the player's pieces (actual player)
            uiHand.removePiece(piece)
        } else {
            //should take from the piece pool (opponent player)
             uiPiecePool.createUIPiece(piece)
        }
        uiPiece ?: return emptyList()
        val square = event.data?.get(EventDataKey.SQUARE) as Int
        if (event.data[EventDataKey.IS_USER_EVENT] == false) {
            // for ai, want to place pieces one by one
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
        return emptyList()
    }

    override fun copy(): Action {
        return PlacedPiece(eventActor, uiBoard, uiHand, uiPiecePool)
    }
}