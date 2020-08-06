package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.ui.util.UIClickListener

class UIHand(handSize: Int): Table() {
    private val handSlots = mutableListOf<UIHandSlot>()

    init {
        for (i in 1 .. handSize) {
            val newSlot = UIHandSlot()
            newSlot.addListener(
                    UIClickListener(newSlot, {
                        if (newSlot.piece == null) return@UIClickListener
                        for (slot in handSlots) {
                            if (slot == newSlot) {
                                slot.setSelected(true)
                            } else {
                                slot.setSelected(false)
                            }
                        }
                    })
            )
            this.add(newSlot).width(480f/handSize)
            handSlots.add(newSlot)
        }
    }

    // returns previously set ui pieces
    fun setNewPieces(pieces: List<UIPiece>): List<UIPiece> {
        if (pieces.size > handSlots.size) {
            throw Exception("Hand size ${handSlots.size} less than pieces size ${pieces.size}")
        }
        val oldUIPieces = mutableListOf<UIPiece>()
        for (slot in handSlots) {
            val oldUIPiece = slot.removePiece()
            if (oldUIPiece != null) {
                oldUIPieces.add(oldUIPiece)
            }

            slot.setSelected(false)
        }

        for ((index, piece) in pieces.withIndex()) {
            val slot = handSlots[index]
            slot.setPiece(piece)
        }
        return oldUIPieces
    }

    fun removePiece(piece: Piece): UIPiece? {
        val slot = handSlots.firstOrNull { it.piece == piece }
        slot?.setSelected(false)
        return slot?.removePiece()
    }

    fun getSelectedPiece(): Piece? {
        val slot = handSlots.firstOrNull { it.selected }
        return slot?.piece
    }

}