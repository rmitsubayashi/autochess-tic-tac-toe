package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIHUD

class UpdateScore(private val uihud: UIHUD)
    : Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.scored) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        uihud.updateScore()
        return emptyList()
    }

    override fun copy(): Action {
        return UpdateScore(uihud)
    }
}