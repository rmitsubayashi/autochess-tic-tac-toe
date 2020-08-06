package com.github.rmitsubayashi.action.phase

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class SetupPhase(eventActor: Player): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.ENTER_SETUP_PHASE) return false
        if (event.actor != eventActor) return false
        if (event.data?.get(EventDataKey.DONE) == true) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        val newHand = player.deck.draw(4)
        player.hand.clear()
        player.hand.addAll(newHand)

        return listOf(
                Event(EventType.HAND_CHANGED, player, null),
                // this marks the end of the automatic actions regarding the setup phase.
                // the player will still place pieces on the board.
                // when the user is done with all manual deck setup, enter setup phase is triggered
                Event(EventType.ENTER_SETUP_PHASE, player, null, mapOf(Pair(EventDataKey.DONE, true)))
        )
    }

    override fun copy(): Action {
        return SetupPhase(eventActor as Player)
    }
}