package com.github.rmitsubayashi.action

import com.github.rmitsubayashi.game.Game

abstract class Action (var eventActor: EventActor) {
    abstract fun conditionMet(game: Game, event: Event): Boolean
    abstract fun execute(game: Game, event: Event, userInputResult: List<EventActor>? = null): List<Event>
    abstract fun copy(): Action
}