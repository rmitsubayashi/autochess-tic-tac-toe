package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventActor
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIHUD

class ToggleSetupPhaseButtons(eventActor: EventActor, private val uihud: UIHUD)
    : Action(eventActor) {
    init {
        // need to run first because when we enter the battle phase,
        // we don't want all the battles to happen before disabling buttons
        priority = 0
    }
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type !in listOf(EventType.enterSetupPhase, EventType.enterBattlePhase)) return false
        if (event.actor != eventActor) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val enabled = event.type == EventType.enterSetupPhase
        uihud.enableSetupFinishedButton(enabled)
        return emptyList()
    }

    override fun copy(): Action {
        return ToggleSetupPhaseButtons(eventActor, uihud)
    }
}