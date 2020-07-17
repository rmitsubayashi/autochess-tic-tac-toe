package com.github.rmitsubayashi.ui.game

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.entity.EmptyEventActor
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class UpdatePieceState(private val board: Board,
                       private val uiBoard: UIBoard, private val uiPiecePool: UIPiecePool)
    : Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        return when (event.type) {
            EventType.pieceDamaged, EventType.pieceDead -> event.actor is Piece
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
                uiBoard.updatePieceState(piece)
            }
            EventType.pieceDead -> {
                val piece = event.actor as Piece
                val uiPiece = uiBoard.removePiece(piece)
                if (uiPiece != null) {
                    uiPiecePool.returnPieceToPool(uiPiece)
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