package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIChoosePiece

class ShowChoosePiece(eventActor: EventActor, private val uiChoosePiece: UIChoosePiece)
    : Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.userInputRequested) return false
        if (event.actor !is Player) return false
        if (event.actor != eventActor) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val newVisibility = event.data?.get(EventDataKey.DONE) == null || event.data[EventDataKey.DONE] == false
        uiChoosePiece.isVisible = newVisibility
        return emptyList()
    }

    override fun copy(): Action {
        return ShowChoosePiece(eventActor, uiChoosePiece)
    }
}