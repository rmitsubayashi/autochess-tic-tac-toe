package com.github.rmitsubayashi.game

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Actor

data class AnimationConfig (
     val animation: Action,
     val actor: Actor?,
     val duration: Float,
     val onAnimate: () -> Unit = {}
)