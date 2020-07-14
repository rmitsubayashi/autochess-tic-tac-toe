package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.game.PiecePool
import com.github.rmitsubayashi.ui.game.UIPiecePool
import com.github.rmitsubayashi.ui.game.UIPlayerPieces

class UpdatePiecePool(
        eventActor: EventActor,
        private val uiPool: UIPiecePool,
        private val piecePool: PiecePool,
        private val uiPlayerPieces: UIPlayerPieces
): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type !in listOf(EventType.reroll, EventType.buyPiece)) return false
        if (event.data == null || event.data[EventDataKey.DONE] != true) return false
        if (event.type == EventType.buyPiece && event.actedUpon !is Piece) return false
        if (event.actor !is Player) return false
        if (event.actor != eventActor) return false
        return true
    }


    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        when (event.type) {
            EventType.buyPiece -> {
                val piece = event.actedUpon as Piece
                val uiPiece = uiPool.takePiece(piece)
                if (uiPiece != null) {
                    uiPlayerPieces.addPiece(uiPiece)
                }
            }
            EventType.reroll -> {
                val player = event.actor as Player
                val newPieces = piecePool.getPieces(player)
                uiPool.refreshPieces(newPieces)
            }
            else -> {}
        }

        return emptyList()
    }
    override fun copy(): Action {
        return UpdatePiecePool(eventActor, uiPool, piecePool, uiPlayerPieces)
    }
}