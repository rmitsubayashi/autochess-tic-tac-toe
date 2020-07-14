package com.github.rmitsubayashi.ui.util

import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import java.util.*
import kotlin.concurrent.thread

class UIClickListener(private val actor: Actor, private val onClick: () -> Unit, private val sound: Sound? = null): ClickListener() {
    override fun clicked(event: InputEvent?, x: Float, y: Float) {
        val actors = mutableSetOf(actor)
        var parent: Group? = actor.parent
        while (parent != null) {
            actors.add(parent)
            parent = parent.parent
        }
        val groupQueue = LinkedList<Actor>()
        groupQueue.add(actor)
        while (groupQueue.isNotEmpty()) {
            val next = groupQueue.pop()
            actors.add(next)
            if (next is Group) {
                groupQueue.addAll(next.children)
            }
        }

        for (a in actors) {
            val listeners = a.listeners
            val longClickListener = listeners?.firstOrNull { it is UILongClickListener }
            if (longClickListener != null) {
                if ((longClickListener as UILongClickListener).longPressed) {
                    // if there is a long click, we don't want to do both the long click and click action
                    return
                }
            }
        }
        if (sound != null) thread(name="Sound") { sound.play() }
        onClick()
    }

}