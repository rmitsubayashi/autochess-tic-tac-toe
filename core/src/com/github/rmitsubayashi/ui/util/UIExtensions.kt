package com.github.rmitsubayashi.ui.util

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import kotlin.math.round

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

fun round2(x: Float) = round(x * 10) / 10

fun Table.setBackgroundColor(color: Color) {
    val backgroundColor = BackgroundColor("image/white.png")
    backgroundColor.setColor(color)
    this.background = backgroundColor
}

fun Actor.centerInParent() {
    x = (480f - width) / 2
    y = (800f - height) / 2
}
