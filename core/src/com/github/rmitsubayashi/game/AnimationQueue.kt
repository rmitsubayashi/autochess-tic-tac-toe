package com.github.rmitsubayashi.game

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction

// shows animations one by one
class AnimationQueue {
    // consider using AfterAction or RunnableAction w/ coroutine delays
    // if we use actions other than temporalaction
    private val animationList = mutableListOf<TemporalAction>()

    fun addAnimation(actor: Actor, action: TemporalAction) {
        action.actor = actor
        animationList.add(action)
    }

    // todo should we pass in what to do after the animation is done?
    // for example going to the next player's turn after battle animations
    fun playQueuedAnimations() {
        var secondsDelay = 0f
        for (animation in animationList) {
            val actor = animation.actor
            actor.addAction(
                    Actions.sequence(
                            Actions.delay(secondsDelay),
                            animation
                    )
            )
            secondsDelay += animation.duration
        }
        animationList.clear()
    }
}