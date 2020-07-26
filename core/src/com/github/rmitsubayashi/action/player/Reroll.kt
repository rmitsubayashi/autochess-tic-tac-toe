package com.github.rmitsubayashi.action.player

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class Reroll(eventActor: EventActor): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.reroll) return false
        if (event.data != null && event.data[EventDataKey.DONE] == true) return false
        if (eventActor !is Player) return false
        if (event.actor != eventActor) return false
        if ((event.actor as Player).money < game.piecePool.getRerollCost()) return false

        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        game.piecePool.refresh(player)
        player.money -= game.piecePool.getRerollCost()
        return listOf(
                Event(EventType.moneyChanged, event.actor, null),
                Event(EventType.reroll, event.actor, null, mapOf(Pair(EventDataKey.DONE, true)))
        )
    }

    override fun copy(): Action {
        return Reroll(eventActor)
    }
}