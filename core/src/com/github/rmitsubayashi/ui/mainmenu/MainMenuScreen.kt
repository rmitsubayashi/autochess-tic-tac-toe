package com.github.rmitsubayashi.ui.mainmenu

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.GdxGame
import com.github.rmitsubayashi.ui.game.GameScreen
import com.github.rmitsubayashi.ui.util.Fonts
import com.github.rmitsubayashi.ui.util.IStageScreen
import com.github.rmitsubayashi.ui.util.UIClickListener

class MainMenuScreen(game: GdxGame): IStageScreen(game) {
    init {
        val menuTable = Table().pad(10f).apply {
            setFillParent(true)
        }
        val singlePlayer = createMenuItem("Single Player") {
            game.setScreen(GameScreen(game))
            dispose()
        }
        singlePlayer.debug()
        menuTable.add(singlePlayer).row()
        val multiplayer = createMenuItem("Online") { }
        menuTable.add(multiplayer).row()
        stage.addActor(menuTable)
    }
    private fun createMenuItem(text: String, function: () -> Unit): Table {
        val table = Table().pad(30f)
        table.add(Label(text, Fonts.h1())).minWidth(200f)
        table.touchable = Touchable.enabled
        table.addListener(UIClickListener(table, { function() }))
        return table
    }
}