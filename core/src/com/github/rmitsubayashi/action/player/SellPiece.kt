package com.github.rmitsubayashi.action.player

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventActor
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class SellPiece(eventActor: EventActor): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.sellPiece) return false
        if (event.actor !is Player) return false
        if (eventActor != event.actor) return false
        if (event.actedUpon !is Piece) return false
        if (!event.actor.pieces.contains(event.actedUpon)
                || !game.board.contains(event.actedUpon)) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        val player = event.actor as Player
        val piece = event.actedUpon as Piece
        game.piecePool.putBackInPool(piece)
        player.pieces.remove(piece)
        player.money += piece.cost
        return listOf(
                Event(EventType.moneyChanged, event.actor, null)
        )
    }

    override fun copy(): Action {
        return SellPiece(eventActor)
    }
}