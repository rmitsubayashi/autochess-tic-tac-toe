package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.ui.util.appSkin

class UITurnDisplay(private val player1: Player): Table() {
    private val playerNumberLabel: Label

    init {
        val labelStyle = appSkin.get("big", Label.LabelStyle::class.java)
        playerNumberLabel = Label("", labelStyle)
        this.add(playerNumberLabel)
        width = 300f
        height = 50f
        this.background = appSkin.getDrawable("text-field").apply {
            width = 300f
            height = 50f
        }
    }

    fun setDisplay(player: Player) {
        val playerInt = if (player == player1) 1 else 2
        setPlayerNumberLabel(playerInt)

    }

    private fun setPlayerNumberLabel(playerNumber: Int) {
        playerNumberLabel.setText("Player $playerNumber's turn")
    }
}