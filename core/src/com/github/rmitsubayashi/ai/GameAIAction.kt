package com.github.rmitsubayashi.ai

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class GameAIAction(eventActor: EventActor, private val ai: GameAI): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.enterSetupPhase) return false
        if (event.data?.get(EventDataKey.DONE) != true) return false
        if (event.actor !is Player) return false
        if (event.actor != eventActor) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        val player = event.actor as Player
        // rolls and places pieces on the board
        ai.execute(game, player)
        game.gameProgressManager.toBattlePhase()
        return emptyList()
    }

    override fun copy(): Action {
        return GameAIAction(eventActor, ai)
    }
}