package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.ui.util.appSkin

class UIPieceInfoTooltip: Table() {
    private val pieceName = Label("", appSkin)
    private val pieceAbility = Label("", appSkin.get("small", Label.LabelStyle::class.java))
    private val pieceAbilityCell: Cell<Label>
    private val pieceStats = Label("", appSkin)
    init {
        this.add(pieceName).row()
        pieceAbilityCell = this.add(pieceAbility)
        pieceAbilityCell.align(Align.center).row()
        this.add(pieceStats).row()
        this.pad(8f)
        this.isVisible = false
    }

    fun setTooltipText(piece: Piece, isOnBoard: Boolean) {
        pieceName.setText(piece.name)
        pieceAbility.setText(piece.ability)
        pieceStats.setText(formatStats(piece, isOnBoard))
        pack()
        background = appSkin.getDrawable("half-tone-box")
        width = MathUtils.clamp(width, 240f, 400f)
        pieceAbilityCell.width(width-16f)
        pieceAbility.setWrap(true)
    }

    private fun formatStats(piece: Piece, isOnBoard: Boolean): String {
        val hp = if (isOnBoard) {
            "${piece.currHP}/${piece.currStats.hp}"
        }  else {
            piece.stats.hp.toString()
        }

        val attack = if (isOnBoard) {
            piece.currStats.attack
        } else {
            piece.stats.attack
        }

        val cost = if (isOnBoard) {
            ""
        } else {
            "  Cst: ${piece.cost}"
        }
        return "HP: $hp  Atk: $attack$cost"
    }
}