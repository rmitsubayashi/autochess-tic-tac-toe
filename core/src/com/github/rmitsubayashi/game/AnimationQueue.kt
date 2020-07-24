package com.github.rmitsubayashi.game

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.ui.util.round2

// shows animations one by one
class AnimationQueue() {
    private lateinit var stage: Stage
    private val animationList = mutableListOf<AnimationConfig>()
    private var secondsDelay = 0f

    fun setStage(stage: Stage) {
        this.stage = stage
    }

    fun addAnimation(animationConfig: AnimationConfig) {
        animationList.add(animationConfig)
    }

    fun playQueuedAnimations(afterAnimating: (() -> Unit)? = null) {
        for (animation in animationList) {
            val actor = animation.actor
            if (actor != null) {
                // same code as null condition
                actor.addAction(
                        Actions.sequence(
                                Actions.delay(secondsDelay),
                                animation.animation,
                                Actions.run {
                                    secondsDelay -= animation.duration
                                    secondsDelay = round2(secondsDelay)
                                    animation.onAnimate()
                                }
                        )
                )
            } else {
                stage.addAction(
                        Actions.sequence(
                                Actions.delay(secondsDelay),
                                animation.animation,
                                Actions.run {
                                    secondsDelay -= animation.duration
                                    secondsDelay = round2(secondsDelay)
                                    animation.onAnimate()
                                }
                        )
                )
            }
            secondsDelay += animation.duration
            secondsDelay = round2(secondsDelay)
        }
        if (afterAnimating != null) {
            stage.addAction(
                    Actions.sequence(
                            Actions.delay(secondsDelay),
                            Actions.run {
                                afterAnimating()
                            }
                    )
            )
        }

        animationList.clear()
    }

    fun isAnimating() = secondsDelay > 0
}