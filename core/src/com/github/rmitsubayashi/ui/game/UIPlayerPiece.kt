package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Piece

class UIPlayerPiece(private val uiPiece: UIPiece): Table() {
    val selected get() = _selected
    private var _selected = false
    val piece: Piece = uiPiece.actualPiece ?: throw Exception("need to pass actual piece")

    init {
        this.add(uiPiece)
    }

    fun setSelected(selected: Boolean) {
        val diff = selected != _selected
        if (diff) {
            _selected = selected
            background = if (selected) {
                Image(Texture("badlogic.jpg")).drawable
            } else {
                Image(Texture("tictactoe.jpg")).drawable
            }
        }
    }

    fun removePiece(): UIPiece {
        this.clear()
        this.remove()
        return uiPiece
    }
}