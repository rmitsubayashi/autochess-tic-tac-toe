package com.github.rmitsubayashi.action.player

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class PlacePiece(eventActor: EventActor): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.placePiece) return false
        if (event.actor !is Player) return false
        if (event.actedUpon !is Piece) return false
        if (!event.actor.pieces.contains(event.actedUpon)) return false
        val square = event.data?.get(EventDataKey.SQUARE)
        if (square !is Int) return false
        if (game.board[square] != null) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        val square = event.data?.get(EventDataKey.SQUARE) as Int
        val player = event.actor as Player
        val piece = event.actedUpon as Piece
        game.board[square] = piece
        player.pieces.remove(piece)
        return listOf(Event(EventType.placePiece, piece, null,
                mapOf(Pair(EventDataKey.DONE, true), Pair(EventDataKey.SQUARE, square))))
    }

    override fun copy(): Action {
        return PlacePiece(eventActor)
    }


}