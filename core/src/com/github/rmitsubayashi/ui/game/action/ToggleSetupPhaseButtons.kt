package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIHUD
import com.github.rmitsubayashi.ui.game.UIPiecesToggle

class ToggleSetupPhaseButtons(eventActor: Player, private val uihud: UIHUD, private val uiPiecesToggle: UIPiecesToggle)
    : Action(eventActor) {
    init {
        // need to run first because when we enter the battle phase,
        // we don't want all the battles to happen before disabling buttons
        priority = 0
    }
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type !in listOf(EventType.ENTER_DECK_BUILDING_PHASE, EventType.ENTER_SETUP_PHASE, EventType.enterBattlePhase)) return false
        if (event.actor != eventActor) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val enabled = event.type == EventType.ENTER_SETUP_PHASE || event.type == EventType.ENTER_DECK_BUILDING_PHASE
        uihud.enableSetupFinishedButton(enabled)
        if (event.type == EventType.ENTER_DECK_BUILDING_PHASE) {
            uihud.setSetupFinishedButtonText("Finish building deck")
            uiPiecesToggle.clickShop()
        }
        if (event.type == EventType.ENTER_SETUP_PHASE) {
            uihud.setSetupFinishedButtonText("Finish placing pieces")
            uiPiecesToggle.clickHand()
        }

        return emptyList()
    }

    override fun copy(): Action {
        return ToggleSetupPhaseButtons(eventActor as Player, uihud, uiPiecesToggle)
    }
}