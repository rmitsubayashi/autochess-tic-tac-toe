package com.github.rmitsubayashi.ai

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class AISetupPhase(eventActor: EventActor, private val ai: GameAI): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.ENTER_DECK_BUILDING_PHASE) return false
        if (event.data?.get(EventDataKey.DONE) != true) return false
        if (event.actor !is Player) return false
        if (event.actor != eventActor) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        // steps through setup phase as well
        ai.execute(game, player)
        game.animationQueue.playQueuedAnimations {
            game.gameProgressManager.toBattlePhase()
        }
        return emptyList()
    }

    override fun copy(): Action {
        return AISetupPhase(eventActor, ai)
    }
}