package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.EmptyEventActor
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game

abstract class PassiveAction: Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        return false
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        return emptyList()
    }
}