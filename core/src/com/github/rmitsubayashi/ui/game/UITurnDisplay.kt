package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.ui.util.setAlpha

class UITurnDisplay(private val player1: Player): Table() {
    private val turnNumberLabel: Label
    private val playerNumberLabel: Label

    init {
        val labelStyle = Label.LabelStyle()
        labelStyle.font = BitmapFont()
        turnNumberLabel = Label("", labelStyle)
        playerNumberLabel = Label("", labelStyle)

        this.add(turnNumberLabel).row()
        this.add(playerNumberLabel)

        this.setAlpha(0f)
    }

    fun setDisplay(turn: Int, player: Player) {
        setTurnNumberLabel(turn)
        val playerInt = if (player == player1) 1 else 2
        setPlayerNumberLabel(playerInt)

    }

    private fun setTurnNumberLabel(turn: Int) {
        turnNumberLabel.setText("Turn $turn")
    }

    private fun setPlayerNumberLabel(playerNumber: Int) {
        playerNumberLabel.setText("Player $playerNumber")
    }
}