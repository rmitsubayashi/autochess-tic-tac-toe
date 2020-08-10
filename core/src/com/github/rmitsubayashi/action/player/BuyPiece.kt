package com.github.rmitsubayashi.action.player

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class BuyPiece(eventActor: Player): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.buyPiece) return false
        if (event.actor !is Player) return false
        if (eventActor != event.actor) return false
        if (event.actedUpon !is Piece) return false
        val poolPieces = game.getShop(event.actor)?.lastRolledPieces
        val piece = event.actedUpon
        if (poolPieces == null || !poolPieces.contains(piece)) return false
        if (event.actedUpon.cost > event.actor.money) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val pieceToBuy = event.actedUpon as Piece
        val player = event.actor as Player

        pieceToBuy.player = player
        player.deck.add(pieceToBuy)
        player.money -= pieceToBuy.cost
        game.getShop(player)?.takeFromPool(pieceToBuy, player)

        return listOf(
                Event(EventType.moneyChanged, event.actor, null),
                Event(EventType.buyPiece, event.actor, pieceToBuy, mapOf(Pair(EventDataKey.DONE, true)))
        )
    }

    override fun copy(): Action {
        return BuyPiece(eventActor as Player)
    }
}