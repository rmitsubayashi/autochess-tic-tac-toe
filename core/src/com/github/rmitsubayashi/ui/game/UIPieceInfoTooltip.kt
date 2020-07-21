package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Stats

class UIPieceInfoTooltip: Table() {
    private val pieceName: Label
    private val pieceAbility: Label
    private val pieceStats: Label
    init {
        this.background = Image(Texture("image/badlogic.jpg")).drawable
        val labelStyle = Label.LabelStyle()
        labelStyle.font = BitmapFont()
        pieceName = Label("", labelStyle)
        pieceAbility = Label("", labelStyle)
        pieceStats = Label("", labelStyle)

        this.add(pieceName).row()
        this.add(pieceAbility).row()
        this.add(pieceStats).row()
        this.isVisible = false
    }

    fun showTooltip(piece: Piece) {
        pieceName.setText(piece.name)
        pieceAbility.setText(piece.ability)
        pieceStats.setText(formatStats(piece.stats))
        this.isVisible = true
    }

    fun hideTooltip() {
        this.isVisible = false
    }

    private fun formatStats(stats: Stats): String {
        return "HP: ${stats.hp}  Atk: ${stats.attack}"
    }
}