package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIPiecePool
import com.github.rmitsubayashi.ui.game.UIPlayerPieces

class SoldPiece(eventActor: EventActor, private val uiPlayerPieces: UIPlayerPieces, private val uiPiecePool: UIPiecePool)
    : Action(eventActor){
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.sellPiece) return false
        if (event.actor !is Player) return false
        if (event.actedUpon !is Piece) return false
        if (event.data?.get(EventDataKey.DONE) != true) return false

        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        val piece = event.actedUpon as Piece
        val uiPiece = uiPlayerPieces.removePiece(piece)
        if (uiPiece != null) {
            uiPiecePool.returnPieceToPool(uiPiece)
        }
        return emptyList()
    }

    override fun copy(): Action {
        return SoldPiece(eventActor, uiPlayerPieces, uiPiecePool)
    }
}