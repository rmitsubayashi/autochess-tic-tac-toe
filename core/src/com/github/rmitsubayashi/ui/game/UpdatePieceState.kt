package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.action.EmptyEventActor
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.AnimationConfig
import com.github.rmitsubayashi.game.Game

class UpdatePieceState(private val board: Board,
                       private val uiBoard: UIBoard, private val uiPiecePool: UIPiecePool)
    : Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        return when (event.type) {
            EventType.pieceDamaged -> event.actor is Piece
            EventType.enterSetupPhase -> {
                event.data?.get(EventDataKey.DONE) == true &&
                        event.actor is Player
            }
            else -> false
        }

    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        when (event.type) {
            EventType.pieceDamaged -> {
                val piece = event.actor as Piece
                if (piece.isDead()) {
                    val uiPiece = uiBoard.findPiece(piece)
                    uiPiece ?: return emptyList()
                    game.animationQueue.addAnimation(
                            AnimationConfig(
                                    Actions.alpha(0f, 0.3f),
                                    uiPiece,
                                    0.3f
                            ) {
                                val toRemoveUIPiece = uiBoard.removePiece(piece)
                                if (toRemoveUIPiece != null) {
                                    uiPiecePool.returnPieceToPool(toRemoveUIPiece)
                                }
                            }
                    )
                } else {
                    uiBoard.updatePieceState(piece)
                }
            }
            EventType.enterSetupPhase -> {
                // we don't know which pieces have been newly secured,
                // so just update all of them
                val player = event.actor as Player
                val playerPieces = board.filter { it?.player == player }.filterNotNull()
                for (piece in playerPieces) {
                    uiBoard.updatePieceState(piece)
                }
            }
            else -> {}
        }
        return emptyList()
    }

    override fun copy(): Action {
        return UpdatePieceState(board, uiBoard, uiPiecePool)
    }
}