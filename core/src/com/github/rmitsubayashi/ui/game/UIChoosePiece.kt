package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.util.UIClickListener
import com.github.rmitsubayashi.ui.util.appSkin

class UIChoosePiece(game: Game): Table() {
    private val chooseLabel = Label("Choose a piece", appSkin)
    private val cancelButton = TextButton("cancel", appSkin.get("borderless", TextButton.TextButtonStyle::class.java))

    init {
        chooseLabel
        this.add(chooseLabel).row()
        cancelButton.addListener(
                UIClickListener(
                        cancelButton,
                        {
                            game.userInputManager.cancelUserInput()
                        }
                )
        )
        this.add(cancelButton)

        this.isVisible = false
    }
}