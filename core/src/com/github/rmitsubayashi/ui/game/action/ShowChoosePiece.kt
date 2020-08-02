package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIBoard
import com.github.rmitsubayashi.ui.game.UIChoosePiece

class ShowChoosePiece(eventActor: EventActor, private val uiChoosePiece: UIChoosePiece, private val uiBoard: UIBoard)
    : Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.userInputRequested) return false
        if (event.actor !is Player) return false
        if (event.actor != eventActor) return false
        if (event.data?.get(EventDataKey.DONE) == null && event.data?.get(EventDataKey.SQUARE) == null) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val done = event.data?.get(EventDataKey.DONE) == true
        val newVisibility = !done
        uiChoosePiece.isVisible = newVisibility

        if (done) {
            uiBoard.removeHighlights()
        } else {
            @Suppress("UNCHECKED_CAST")
            val squares = event.data?.get(EventDataKey.SQUARE) as List<Int>
            for (i in squares) {
                uiBoard.highlightSquare(i)
            }
        }
        return emptyList()
    }

    override fun copy(): Action {
        return ShowChoosePiece(eventActor, uiChoosePiece, uiBoard)
    }
}