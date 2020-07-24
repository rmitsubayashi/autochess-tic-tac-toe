package com.github.rmitsubayashi.game

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.ui.util.round2

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
                                secondsDelay = round2(secondsDelay)
                            }
                    )
            )
            secondsDelay += animation.duration
            secondsDelay = round2(secondsDelay)
        }

        animationList.clear()
    }

    fun isAnimating() = secondsDelay > 0
}