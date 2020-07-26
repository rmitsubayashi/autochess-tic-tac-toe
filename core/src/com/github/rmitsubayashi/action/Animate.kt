package com.github.rmitsubayashi.action

import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game

class Animate: Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        return event.type == EventType.shouldAnimate
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        game.animationQueue.playQueuedAnimations()
        return emptyList()
    }

    override fun copy(): Action {
        return Animate()
    }
}