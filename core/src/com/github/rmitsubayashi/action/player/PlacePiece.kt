package com.github.rmitsubayashi.action.player

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class PlacePiece(eventActor: EventActor): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.placePiece) return false
        if (event.actor !is Player) return false
        if (event.actor != eventActor) return false
        if (event.actedUpon !is Piece) return false
        if (!event.actor.deck.contains(event.actedUpon)) return false
        val square = event.data?.get(EventDataKey.SQUARE)
        if (square !is Int) return false
        if (game.board[square] != null) return false
        if (game.userInputManager.isWaitingForUserInput()) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        if (game.userInputManager.isWaitingForUserInput()) {
            game.userInputManager.setBlockedAction(this)
            return emptyList()
        }
        val square = event.data?.get(EventDataKey.SQUARE) as Int
        val player = event.actor as Player
        val piece = event.actedUpon as Piece
        game.board[square] = piece
        player.deck.remove(piece)
        val isUserEvent = event.data[EventDataKey.IS_USER_EVENT]
        val data = mutableMapOf(Pair(EventDataKey.DONE, true), Pair(EventDataKey.SQUARE, square))
        if (isUserEvent == false) data[EventDataKey.IS_USER_EVENT] = false
        return listOf(
                Event(EventType.placePiece, piece, null, data)
        )
    }

    override fun copy(): Action {
        return PlacePiece(eventActor)
    }


}