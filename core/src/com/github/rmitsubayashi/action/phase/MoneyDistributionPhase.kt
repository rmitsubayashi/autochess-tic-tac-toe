package com.github.rmitsubayashi.action.phase

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.game.MoneyCalculator

class MoneyDistributionPhase(eventActor: Player): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.ENTER_MONEY_DISTRIBUTION_PHASE) return false
        if (event.actor != eventActor) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        //add money for that turn
        player.money = player.money + MoneyCalculator.calculateStartOfTurnMoney(player)
        return listOf(
                Event(EventType.moneyChanged, player, null),
                Event(EventType.shouldAnimate, null, null),
                Event(EventType.ENTER_DECK_BUILDING_PHASE, player, null)
        )
    }

    override fun copy(): Action {
        return MoneyDistributionPhase(eventActor as Player)
    }
}