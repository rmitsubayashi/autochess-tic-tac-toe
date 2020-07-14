package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventActor
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIHUD

class UpdateMoney(eventActor: EventActor, private val uiHUD: UIHUD): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.moneyChanged) return false
        if (event.actor !is Player) return false
        if (event.actor != eventActor) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        uiHUD.updatePlayerMoney()
        return emptyList()
    }

    override fun copy(): Action {
        return UpdateMoney(eventActor, uiHUD)
    }
}