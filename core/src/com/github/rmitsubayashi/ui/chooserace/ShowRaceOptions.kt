package com.github.rmitsubayashi.ui.chooserace

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.entity.Race
import com.github.rmitsubayashi.game.Game

class ShowRaceOptions(player: Player, private val uiRaceOptions: UIRaceOptions)
    : Action(player) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.CHOOSE_RACE) return false
        if (event.actor != eventActor) return false
        if (event.data?.get(EventDataKey.DONE) == true) return false
        if (event.data?.get(EventDataKey.RACES) !is List<*>) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        @Suppress("UNCHECKED_CAST")
        val options = event.data?.get(EventDataKey.RACES) as List<Race>
        uiRaceOptions.setOptions(options)
        return emptyList()
    }

    override fun copy(): Action {
        return ShowRaceOptions(eventActor as Player, uiRaceOptions)
    }
}