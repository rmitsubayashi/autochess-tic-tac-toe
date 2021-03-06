package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece

class UserInputManager(private val game: Game) {
    private var actionWaitingForUserInput: Action? = null
    private var eventForWaitingAction: Event? = null
    var possibleTargets: MutableList<Piece> = mutableListOf()
    private var actionBlockedByUserInputAction: Action? = null

    fun waitForUserInput(action: Action, event: Event, possibleTargets: List<Piece>) {
        actionWaitingForUserInput = action
        eventForWaitingAction = event
        this.possibleTargets.clear()
        this.possibleTargets.addAll(possibleTargets)
        val possibleTargetIndexes = if (possibleTargets.isEmpty()) {
            // assuming if list is empty, we are choosing any empty square
            game.board.mapIndexed {
                index, piece -> if (piece == null) { index } else { -1 }
            }.filter { it != -1 }
        } else {
            this.possibleTargets.map {
                game.board.indexOf(it)
            }.filter { it != -1 }
        }
        // assuming player input only occurs on their turn
        val currPlayer = game.gameProgressManager.currPlayer
        game.notifyEvent(
                Event(EventType.userInputRequested, currPlayer, null,
                mapOf(Pair(EventDataKey.SQUARE, possibleTargetIndexes)))
        )
    }

    fun isWaitingForUserInput() = actionWaitingForUserInput != null

    fun setBlockedAction(action: Action) {
        // don't need the event because it should be the same as
        // the action that is blocking it
        actionBlockedByUserInputAction = action
    }

    fun handleActionWaitingForUserInput(userInput: Piece?) {
        if (!possibleTargets.contains(userInput)) {
            // todo actual error handling
            return
        }
        val tempAction = actionWaitingForUserInput
        val tempEvent = eventForWaitingAction ?: return
        actionWaitingForUserInput = null
        eventForWaitingAction = null
        val newEvents = mutableListOf<Event>()
        newEvents.add(
                Event(
                        EventType.userInputRequested, game.gameProgressManager.currPlayer, null,
                        mapOf(Pair(EventDataKey.DONE, true))
                )
        )
        newEvents.addAll(tempAction?.execute(game, tempEvent, userInput) ?: emptyList())
        val tempAction2 = actionBlockedByUserInputAction
        actionBlockedByUserInputAction = null
        newEvents.addAll(tempAction2?.execute(game, tempEvent) ?: emptyList())
        game.actionObservable.notifyAllActions(newEvents)
    }

    fun cancelUserInput() {
        actionWaitingForUserInput = null
        eventForWaitingAction = null
        actionBlockedByUserInputAction = null
        possibleTargets.clear()

        game.notifyEvent(
                Event(
                        EventType.userInputRequested, game.gameProgressManager.currPlayer, null,
                        mapOf(Pair(EventDataKey.DONE, true))
                )
        )
    }
}