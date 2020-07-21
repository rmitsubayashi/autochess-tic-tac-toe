package com.github.rmitsubayashi.action

import com.github.rmitsubayashi.game.Game

class Animate: Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        return event.type == EventType.shouldAnimate
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        game.animationQueue.playQueuedAnimations()
        return emptyList()
    }

    override fun copy(): Action {
        return Animate()
    }
}