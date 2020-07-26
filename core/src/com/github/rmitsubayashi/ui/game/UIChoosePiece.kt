package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.util.UIClickListener

class UIChoosePiece(game: Game): Table() {
    private val chooseLabel: Label
    private val cancelButton: Button

    init {
        val labelStyle = Label.LabelStyle()
        labelStyle.font = BitmapFont()
        chooseLabel = Label("Choose a piece", labelStyle)
        this.add(chooseLabel).row()
        val textButtonStyle = TextButton.TextButtonStyle()
        textButtonStyle.font = BitmapFont()
        cancelButton = TextButton("cancel", textButtonStyle)
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