package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.ui.util.UIClickListener

class UIPlayerPieces: Table() {
    private val pieceSlots = mutableListOf<UIPlayerPiece>()
    init {
        width = Gdx.graphics.width.toFloat()
        height = 50f
    }

    fun addPiece(piece: UIPiece) {
        val newSlot = UIPlayerPiece(piece)
        newSlot.addListener(UIClickListener(newSlot,
            {
                for(slot in pieceSlots) {
                    if (slot == newSlot) {
                        slot.setSelected(true)
                    } else {
                        slot.setSelected(false)
                    }
                }
            }
        ))
        newSlot.width = 50f
        newSlot.height = 50f
        pieceSlots.add(newSlot)
        this.add(newSlot)
    }

    fun removePiece(piece: Piece): UIPiece? {
        val slot = pieceSlots.firstOrNull { it.piece.isSamePieceType(piece)}
        pieceSlots.remove(slot)
        return slot?.removePiece()
    }

    fun getSelectedPiece(): Piece? {
        val slot = pieceSlots.firstOrNull { it.selected }
        return slot?.piece
    }
}