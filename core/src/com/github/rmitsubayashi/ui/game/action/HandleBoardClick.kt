package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.game.GameProgressManager
import com.github.rmitsubayashi.ui.game.UIHand

class HandleBoardClick(eventActor: Player, private val uiHand: UIHand)
    : Action(eventActor){
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.boardClicked) return false
        if (event.data?.get(EventDataKey.SQUARE) !is Int) return false
        // any interaction with the board must occur in the player's setup phase
        if (game.gameProgressManager.currPlayer != event.actor) return false
        if (game.gameProgressManager.phase != GameProgressManager.Phase.SETUP) return false
        if (game.animationQueue.isAnimating()) return false

        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val square = event.data?.get(EventDataKey.SQUARE) as Int
        val squarePiece = game.board[square]
        // the player is choosing a piece for a piece's ability
        if (game.userInputManager.isWaitingForUserInput()) {
            game.userInputManager.handleActionWaitingForUserInput(squarePiece)
            return emptyList()
        }
        val playerPieceSelected = uiHand.getSelectedPiece()
        // player has selected a piece to place on the board
        if (
                event.actor == playerPieceSelected?.player
                && squarePiece == null
        ) {
            return listOf(
                    Event(EventType.placePiece, event.actor, playerPieceSelected,
                    mapOf(Pair(EventDataKey.SQUARE, square)))
            )
        }
        return emptyList()
    }

    override fun copy(): Action {
        return HandleBoardClick(eventActor as Player, uiHand)
    }
}