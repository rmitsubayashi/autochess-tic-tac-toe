package com.github.rmitsubayashi.ai

import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.entity.Race
import com.github.rmitsubayashi.game.Game

class CrudeAIChooseRace: GameAI {
    override fun execute(game: Game, player: Player, event: Event) {
        @Suppress("UNCHECKED_CAST")
        val options = event.data?.get(EventDataKey.RACES) as List<Race>
        val randomOption = options.random()
        game.notifyEvent(
                Event(EventType.CHOOSE_RACE, player, null,
                        mapOf(Pair(EventDataKey.DONE, true), Pair(EventDataKey.RACES, randomOption)))
        )
    }
}