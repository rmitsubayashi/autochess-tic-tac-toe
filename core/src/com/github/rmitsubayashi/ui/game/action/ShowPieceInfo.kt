package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIPieceInfoTooltip

class ShowPieceInfo(eventActor: EventActor, private val tooltip: UIPieceInfoTooltip): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.pieceLongClicked) return false
        if (event.actor !is Piece) return false

        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val notReleased = event.data == null || event.data[EventDataKey.DONE] != true
        if (notReleased) {
            val piece = event.actor as Piece
            tooltip.showTooltip(piece)
        } else {
            tooltip.hideTooltip()
        }
        return emptyList()
    }

    override fun copy(): Action {
        return ShowPieceInfo(eventActor, tooltip)
    }
}