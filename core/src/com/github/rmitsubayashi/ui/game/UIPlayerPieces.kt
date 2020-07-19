package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.util.UIClickListener
import com.github.rmitsubayashi.ui.util.removeActorAndUpdateCellStructure

class UIPlayerPieces(game: Game): Table() {
    private val pieceSlots = mutableListOf<UIPlayerPiece>()
    private val sellButton: TextButton

    init {
        val textButtonStyle = TextButton.TextButtonStyle()
        textButtonStyle.font = BitmapFont()
        sellButton = TextButton("sell", textButtonStyle)
        sellButton.addListener(
                UIClickListener(sellButton, {
                    //for now we can't sell pieces on board
                    val selectedPiece = getSelectedPiece() ?: return@UIClickListener
                    game.actionObservable.notifyAllActions(
                            Event(EventType.sellPiece, game.player1, selectedPiece)
                    )
                })
        )
        this.add(sellButton)
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

        pieceSlots.add(newSlot)
        this.add(newSlot).width(100f)
    }

    fun removePiece(piece: Piece): UIPiece? {
        val slot = pieceSlots.firstOrNull { it.piece.isSamePieceType(piece)}
        pieceSlots.remove(slot)
        val uiPiece = slot?.removePiece()
        removeActorAndUpdateCellStructure(slot)
        return uiPiece
    }

    fun getSelectedPiece(): Piece? {
        val slot = pieceSlots.firstOrNull { it.selected }
        return slot?.piece
    }
}