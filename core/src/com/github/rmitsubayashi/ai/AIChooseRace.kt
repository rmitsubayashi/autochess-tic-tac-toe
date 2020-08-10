package com.github.rmitsubayashi.ai

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class AIChooseRace(player: Player, private val ai: GameAI): Action(player) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.CHOOSE_RACE) return false
        if (event.actor != eventActor) return false
        if (event.data?.get(EventDataKey.DONE) == true) return false
        if (event.data?.get(EventDataKey.RACES) !is List<*>) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        ai.execute(game, player, event)
        return emptyList()
    }

    override fun copy(): Action {
        return AIChooseRace(eventActor as Player, ai)
    }
}