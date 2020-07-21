package com.github.rmitsubayashi.ui.util

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table

// using the default removeActor() keeps the table structure..
// so if there is a table like [A][B][C] and we remove B,
// the result will be like [A][ ][C]
fun Table.removeActorAndUpdateCellStructure(actor: Actor?) {
    val cell = getCell(actor)
    actor?.remove()
    cells.removeValue(cell, true)
    invalidate()
}

fun Actor.setAlpha(alpha: Float) {
    val color = this.color
    color.a = alpha
    this.color = color
}