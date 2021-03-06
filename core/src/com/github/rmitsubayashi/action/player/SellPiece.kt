package com.github.rmitsubayashi.action.player

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class SellPiece(eventActor: Player): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.sellPiece) return false
        if (event.actor !is Player) return false
        if (eventActor != event.actor) return false
        if (event.actedUpon !is Piece) return false
        if (!event.actor.deck.contains(event.actedUpon)) return false
        if (event.data?.get(EventDataKey.DONE) == true) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        val piece = event.actedUpon as Piece
        game.getShop(player)?.putBackInPool(piece)
        player.deck.remove(piece)
        player.money += piece.cost
        game.actionObservable.unsubscribeActions(piece.actions)
        return listOf(
                Event(EventType.moneyChanged, event.actor, null),
                Event(EventType.sellPiece, event.actor, piece, mapOf(Pair(EventDataKey.DONE, true)))
        )
    }

    override fun copy(): Action {
        return SellPiece(eventActor as Player)
    }
}