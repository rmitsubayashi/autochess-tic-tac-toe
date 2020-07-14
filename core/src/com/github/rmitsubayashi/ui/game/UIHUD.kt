package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Player

// HUD = Head-Up Display
class UIHUD(private val player: Player): Table() {
    private val moneyLabel: Label
    init {
        val labelStyle = Label.LabelStyle()
        labelStyle.font = BitmapFont()
        moneyLabel = Label(player.money.toString(), labelStyle)
        this.add(moneyLabel)
    }

    fun updatePlayerMoney() {
        moneyLabel.setText(player.money.toString())
    }
}