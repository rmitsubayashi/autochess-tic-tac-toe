package com.github.rmitsubayashi.ui.util

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktx.async.KtxAsync

class UILongClickListener(private val onLongClick: () -> Unit, private val onRelease: () -> Unit)
    : ActorGestureListener() {
    val longPressed get() = _longPressed
    private var _longPressed = false
    override fun longPress(actor: Actor?, x: Float, y: Float): Boolean {
        if (!longPressed) {
            onLongClick()
            _longPressed = true
        }
        return true
    }

    override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
        if (longPressed) {
            onRelease()
            // VERY HACKY.
            // we want the click listener to not fire if
            // a long click event is triggered,
            // but I couldn't think of a way to better
            // communicate with the click listener
            // Delaying makes sure that the click listener
            // can look at this listener and see if a long click
            // has been triggered
            KtxAsync.launch {
                delay(500L)
                _longPressed = false
            }
        } else {
            super.touchUp(event, x, y, pointer, button)
        }
    }


}