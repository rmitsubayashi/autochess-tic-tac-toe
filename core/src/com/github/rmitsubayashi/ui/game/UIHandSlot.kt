package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Scaling
import com.github.rmitsubayashi.entity.Piece

class UIHandSlot: Table() {
    val selected get() = _selected
    private var _selected = false
    private var uiPiece: UIPiece? = null
    val piece : Piece? get() = _piece
    private var _piece: Piece? = null

    fun setSelected(selected: Boolean) {
        if (uiPiece == null) return
        val diff = selected != _selected
        if (diff) {
            _selected = selected
            background = if (selected) {
                Image(Texture("image/badlogic.jpg")).drawable
            } else {
                Image(Texture("image/tictactoe.png")).drawable
            }
        }
    }

    fun setPiece(uiPiece: UIPiece) {
        this._piece = uiPiece.actualPiece
        this.uiPiece = uiPiece
        // should still call removePiece() before setting a piece
        // because we want to return the image back to the pool
        this.clearChildren()
        this.add(uiPiece)
        uiPiece.setScaling(Scaling.fit)
    }

    fun removePiece(): UIPiece? {
        this.clearChildren()
        return uiPiece
    }
}