package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.ui.util.UIClickListener

class UIPlayerPieces: Table() {
    private val pieceSlots = mutableListOf<UIPlayerPiece>()

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

        pieceSlots.add(newSlot)
        this.add(newSlot).width(100f)
        newSlot.debug()
    }

    fun removePiece(piece: Piece): UIPiece? {
        val slot = pieceSlots.firstOrNull { it.piece.isSamePieceType(piece)}
        pieceSlots.remove(slot)
        slot?.remove()
        return slot?.removePiece()
    }

    fun getSelectedPiece(): Piece? {
        val slot = pieceSlots.firstOrNull { it.selected }
        return slot?.piece
    }
}