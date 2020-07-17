package com.github.rmitsubayashi.action

import com.github.rmitsubayashi.game.Game

abstract class Action (var eventActor: EventActor): Comparable<Action> {
    // lower priorities will be called first
    protected var priority = 10
    abstract fun conditionMet(game: Game, event: Event): Boolean
    abstract fun execute(game: Game, event: Event, userInputResult: List<EventActor>? = null): List<Event>
    abstract fun copy(): Action

    override fun compareTo(other: Action): Int {
        return this.priority.compareTo(other.priority)
    }
}