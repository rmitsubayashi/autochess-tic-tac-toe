package com.github.rmitsubayashi.ui.util

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label

object Fonts {
    fun h1(): Label.LabelStyle {
        return Label.LabelStyle().apply {
            font = BitmapFont()
        }
    }
}