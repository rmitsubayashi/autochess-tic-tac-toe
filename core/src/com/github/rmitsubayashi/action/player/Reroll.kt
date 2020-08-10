package com.github.rmitsubayashi.action.player

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class Reroll(eventActor: Player): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.SHOP_REROLL) return false
        if (event.data != null && event.data[EventDataKey.DONE] == true) return false
        if (event.actor != eventActor) return false
        if ((event.actor as Player).money < game.getShop(event.actor)?.getRerollCost() ?: Integer.MAX_VALUE) return false

        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        val shop = game.getShop(player) ?: return emptyList()
        shop.refresh()
        player.money -= shop.getRerollCost()
        return listOf(
                Event(EventType.moneyChanged, event.actor, null),
                Event(EventType.SHOP_REROLL, event.actor, null, mapOf(Pair(EventDataKey.DONE, true)))
        )
    }

    override fun copy(): Action {
        return Reroll(eventActor as Player)
    }
}