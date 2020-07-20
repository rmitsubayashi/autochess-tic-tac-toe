package com.github.rmitsubayashi.game

import com.badlogic.gdx.scenes.scene2d.actions.Actions

// shows animations one by one
class AnimationQueue {
    // consider using AfterAction or RunnableAction w/ coroutine delays
    // if we use actions other than temporalaction
    private val animationList = mutableListOf<AnimationConfig>()

    fun addAnimation(animationConfig: AnimationConfig) {
        animationList.add(animationConfig)
    }

    fun playQueuedAnimations() {
        var secondsDelay = 0f
        for (animation in animationList) {
            val actor = animation.actor
            actor.addAction(
                    Actions.sequence(
                            Actions.delay(secondsDelay),
                            animation.animation,
                            Actions.run { animation.onAnimate() }
                    )
            )
            secondsDelay += animation.duration
        }
        animationList.clear()
    }
}