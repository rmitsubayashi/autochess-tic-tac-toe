package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.game.GameProgressManager
import com.github.rmitsubayashi.ui.game.UIPlayerPieces

class HandleBoardClick(eventActor: EventActor, private val uiPlayerPieces: UIPlayerPieces)
    : Action(eventActor){
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.boardClicked) return false
        if (event.data?.get(EventDataKey.SQUARE) !is Int) return false
        if (event.actor !is Player) return false

        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        val square = event.data?.get(EventDataKey.SQUARE) as Int
        val squarePiece = game.board[square]
        val playerPieceSelected = uiPlayerPieces.getSelectedPiece()
        // player has selected a piece to place on the board.
        // the player can only place during his setup phase
        if (
                event.actor == playerPieceSelected?.player
                && squarePiece == null
                && game.gameProgressManager.phase == GameProgressManager.Phase.SETUP
                && game.gameProgressManager.currPlayer == event.actor
                && !game.animationQueue.isAnimating()
        ) {
            return listOf(
                    Event(EventType.placePiece, event.actor, playerPieceSelected,
                    mapOf(Pair(EventDataKey.SQUARE, square)))
            )
        }
        return emptyList()
    }

    override fun copy(): Action {
        return HandleBoardClick(eventActor, uiPlayerPieces)
    }
}