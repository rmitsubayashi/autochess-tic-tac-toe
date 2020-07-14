package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIBoard
import com.github.rmitsubayashi.ui.game.UIPlayerPieces

class PlacedPiece(eventActor: EventActor,
                  private val uiBoard: UIBoard,
                  private val uiPlayerPieces: UIPlayerPieces)
    : Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.placePiece) return false
        if (event.data?.get(EventDataKey.DONE) != true) return false
        if (event.data[EventDataKey.SQUARE] !is Int) return false
        if (event.actor !is Piece) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        val uiPiece = uiPlayerPieces.removePiece(event.actor as Piece)
        uiPiece ?: return emptyList()
        val square = event.data?.get(EventDataKey.SQUARE) as Int
        uiBoard.placePiece(uiPiece, square)
        return emptyList()
    }

    override fun copy(): Action {
        return PlacedPiece(eventActor, uiBoard, uiPlayerPieces)
    }
}