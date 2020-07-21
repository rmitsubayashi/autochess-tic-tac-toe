package com.github.rmitsubayashi.game

import com.badlogic.gdx.scenes.scene2d.actions.Actions

// shows animations one by one
class AnimationQueue {
    private val animationList = mutableListOf<AnimationConfig>()
    private var secondsDelay = 0f


    fun addAnimation(animationConfig: AnimationConfig) {
        animationList.add(animationConfig)
    }

    fun playQueuedAnimations() {
        for (animation in animationList) {
            val actor = animation.actor
            actor.addAction(
                    Actions.sequence(
                            Actions.delay(secondsDelay),
                            animation.animation,
                            Actions.run {
                                animation.onAnimate()
                                secondsDelay -= animation.duration
                            }
                    )
            )
            secondsDelay += animation.duration
        }

        animationList.clear()
    }
}