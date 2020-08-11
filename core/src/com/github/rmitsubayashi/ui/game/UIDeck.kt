package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.assets.SoundAssets
import com.github.rmitsubayashi.ui.util.UIClickListener
import com.github.rmitsubayashi.ui.util.appSkin
import com.github.rmitsubayashi.ui.util.removeActorAndUpdateCellStructure

class UIDeck(private val assetManager: AssetManager, game: Game): Table() {
    private val pieceSlots = mutableListOf<UIDeckSlot>()
    private val sellButton = TextButton("S", appSkin)
    private val emptyStateButton = Label("No pieces in your deck. Buy more at the shop!", appSkin)

    init {
        sellButton.addListener(
                UIClickListener(sellButton, {
                    //for now we can't sell pieces on board
                    val selectedPiece = getSelectedPiece() ?: return@UIClickListener
                    game.actionObservable.notifyAllActions(
                            Event(EventType.sellPiece, game.player1, selectedPiece)
                    )
                })
        )
        showEmptyState()
    }

    fun addPiece(piece: UIPiece) {
        val newSlot = UIDeckSlot(piece)
        newSlot.addListener(UIClickListener(newSlot,
            {
                for(slot in pieceSlots) {
                    if (slot == newSlot) {
                        slot.setSelected(true)
                    } else {
                        slot.setSelected(false)
                    }
                }
            },
            assetManager.get(SoundAssets.click)
        ))

        pieceSlots.add(newSlot)
        this.clearChildren()
        for (slot in pieceSlots) {
            this.add(slot).width(100f)
        }
        this.add(sellButton)
    }

    fun removePiece(piece: Piece): UIPiece? {
        val slot = pieceSlots.firstOrNull { it.piece.isSamePieceType(piece)}
        pieceSlots.remove(slot)
        val uiPiece = slot?.removePiece()
        if (pieceSlots.isEmpty()) {
            showEmptyState()
        } else {
            removeActorAndUpdateCellStructure(slot)
        }
        return uiPiece
    }

    private fun getSelectedPiece(): Piece? {
        val slot = pieceSlots.firstOrNull { it.selected }
        return slot?.piece
    }

    private fun showEmptyState() {
        this.clearChildren()
        this.add(emptyStateButton)
    }
}