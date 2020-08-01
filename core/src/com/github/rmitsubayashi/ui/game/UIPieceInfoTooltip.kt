package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Stats
import com.github.rmitsubayashi.ui.util.appSkin
import com.github.rmitsubayashi.ui.util.centerInParent

class UIPieceInfoTooltip: Table() {
    private val pieceName = Label("", appSkin)
    private val pieceAbility = Label("", appSkin)
    private val pieceStats = Label("", appSkin)
    init {
        this.add(pieceName).row()
        this.add(pieceAbility).row()
        this.add(pieceStats).row()
        this.isVisible = false
    }

    fun showTooltip(piece: Piece) {
        pieceName.setText(piece.name)
        pieceAbility.setText(piece.ability)
        pieceStats.setText(formatStats(piece.stats))
        pack()
        background = appSkin.getDrawable("half-tone-box")
        this.centerInParent()
        this.isVisible = true
    }

    fun hideTooltip() {
        this.isVisible = false
    }

    private fun formatStats(stats: Stats): String {
        return "HP: ${stats.hp}  Atk: ${stats.attack}"
    }
}