package com.github.rmitsubayashi.action.phase

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class DeckBuildingPhase(eventActor: Player): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.ENTER_DECK_BUILDING_PHASE) return false
        if (event.actor != eventActor) return false
        if (event.data?.get(EventDataKey.DONE) == true) return false

        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        //the player gets new pieces from the pool.
        //note that this is different from a regular roll because no money is spent
        game.piecePool.refresh(player)
        val newEvents = mutableListOf<Event>()
        newEvents.add(
                Event(EventType.SHOP_REROLL, player, null,
                        mapOf(Pair(EventDataKey.DONE, true), Pair(EventDataKey.IS_USER_EVENT, false)))
        )
        newEvents.add(
                // this marks the end of the automatic actions regarding the deck building phase.
                // the user can still buy pieces to put into the deck.
                // when the user is done with all manual deck setup, enter setup phase is triggered
                Event(EventType.ENTER_DECK_BUILDING_PHASE, player, null, mapOf(Pair(EventDataKey.DONE, true)))
        )
        return newEvents
    }

    override fun copy(): Action {
        return DeckBuildingPhase(eventActor as Player)
    }
}