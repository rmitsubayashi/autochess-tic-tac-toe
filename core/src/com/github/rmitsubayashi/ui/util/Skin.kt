package com.github.rmitsubayashi.ui.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin

val appSkin by lazy {
        Skin().apply {
            addRegions(TextureAtlas("skin/comic-ui.atlas"))
            load(Gdx.files.internal("skin/comic-ui.json"))
        }
}