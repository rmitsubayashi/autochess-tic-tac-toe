package com.github.rmitsubayashi.ai

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class AIUserInput(eventActor: EventActor, private val ai: GameAI): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.userInputRequested) return false
        if (event.data?.get(EventDataKey.DONE) == true) return false
        if (event.actor !is Player) return false
        if (event.actor != eventActor) return false
        if (!game.userInputManager.isWaitingForUserInput()) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        ai.execute(game, player)
        return emptyList()
    }

    override fun copy(): Action {
        return AIUserInput(eventActor, ai)
    }
}