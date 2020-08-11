package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.ui.util.appSkin

class UIPhaseDisplay(private val player1: Player): Table() {
    private val phaseLabel: Label

    init {
        val labelStyle = appSkin.get("big", Label.LabelStyle::class.java)
        phaseLabel = Label("", labelStyle)
        this.add(phaseLabel)
        width = 300f
        height = 50f
        this.background = appSkin.getDrawable("text-field").apply {
            width = 300f
            height = 50f
        }
    }

    fun setTurnDisplay(player: Player) {
        val playerInt = if (player == player1) 1 else 2
        setTurnLabel(playerInt)
    }

    private fun setTurnLabel(playerNumber: Int) {
        phaseLabel.setText("Player $playerNumber's turn")
    }
    
    fun setBattlePhaseText() {
        phaseLabel.setText("Battle!")
    }
}