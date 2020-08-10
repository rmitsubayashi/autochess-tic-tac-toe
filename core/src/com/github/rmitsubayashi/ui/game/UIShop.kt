package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.util.UIClickListener
import com.github.rmitsubayashi.ui.util.appSkin

class UIShop(private val game: Game, private val player: Player, private val uiPiecePool: UIPiecePool): Table() {
    private lateinit var pieceSlots: List<UIPiecePoolSlot>

    init {
        val shop = game.getShop(player)
        if (shop != null) {
            allocatePieceSlots(shop.getPieceNumberOfferedOnReroll())
        }
    }

    private fun allocatePieceSlots(slots: Int) {
        val buttonPad = 8f
        this.pad(0f, 0f, 0f, buttonPad)
        val tempList = mutableListOf<UIPiecePoolSlot>()
        val refreshWidth = 50f
        for (i in 1 .. slots) {
            val slot = UIPiecePoolSlot(player, game)
            this.add(slot).width((480f-refreshWidth-buttonPad) / 3)
            tempList.add(slot)
        }
        val rerollButton = TextButton("R", appSkin)
        rerollButton.addListener(
                UIClickListener(
                        rerollButton,
                        {
                            game.actionObservable.notifyAllActions(
                                    Event(EventType.SHOP_REROLL, player, null)
                            )
                        }
                )
        )
        this.add(rerollButton).width(refreshWidth)
        pieceSlots = tempList

    }

    fun refreshPieces(newPieces: List<Piece>) {
        if (newPieces.size != game.getShop(player)?.getPieceNumberOfferedOnReroll()) {
            return
        }
        for ((index, piece) in newPieces.withIndex()) {
            val slot = pieceSlots[index]
            val oldPiece = slot.removePiece()
            if (oldPiece != null) {
                uiPiecePool.returnPieceToPool(oldPiece)
            }
            val uiPiece = uiPiecePool.createUIPiece(piece)
            uiPiece ?: continue
            slot.placePiece(uiPiece)
        }
    }

    fun takePiece(piece: Piece): UIPiece? {
        for (slot in pieceSlots) {
            if (slot.piece?.actualPiece == piece) {
                return slot.removePiece()
            }
        }
        return null
    }
}