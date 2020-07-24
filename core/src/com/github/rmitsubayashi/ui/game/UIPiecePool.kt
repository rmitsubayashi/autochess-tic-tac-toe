package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Pool
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.game.PiecePool
import com.github.rmitsubayashi.ui.util.UIClickListener

// serves as the pool of piece UI objects
// and the part of the UI that shows the pieces the player rolled
class UIPiecePool(private val assetManager: AssetManager, private val game: Game, private val player: Player): Table() {
    // each piece has its own piece pool
    private lateinit var piecePools: List<Pair<Piece, Pool<UIPiece>>>
    private lateinit var pieceSlots: List<UIPiecePoolSlot>

    init {
        populatePool(game.piecePool)
        allocatePieceSlots(game.piecePool.getPieceNumberOfferedOnReroll())
    }

    private fun populatePool(piecePool: PiecePool) {
        val allPieces = piecePool.getAllPieces()
        val tempList = mutableListOf<Pair<Piece, Pool<UIPiece>>>()
        for (piece in allPieces) {
            tempList.add(
                Pair(
                    piece,
                    object: Pool<UIPiece>() {
                        override fun newObject(): UIPiece = UIPiece.create(assetManager, piece, game)
                    }
                )
            )
        }
        piecePools = tempList
    }

    private fun allocatePieceSlots(slots: Int) {
        val tempList = mutableListOf<UIPiecePoolSlot>()
        for (i in 1 .. slots) {
            val slot = UIPiecePoolSlot(player, game)
            this.add(slot).grow().uniform()
            tempList.add(slot)
        }
        val textButtonStyle = TextButton.TextButtonStyle()
        textButtonStyle.font = BitmapFont()
        val rerollButton = TextButton("R", textButtonStyle)
        rerollButton.addListener(
                UIClickListener(
                        rerollButton,
                        {
                            game.actionObservable.notifyAllActions(
                                    Event(EventType.reroll, player, null)
                            )
                        }
                )
        )
        this.add(rerollButton).grow().uniform()
        pieceSlots = tempList

    }

    fun refreshPieces(newPieces: List<Piece>) {
        if (newPieces.size != game.piecePool.getPieceNumberOfferedOnReroll()) {
            return
        }
        for ((index, piece) in newPieces.withIndex()) {
            val slot = pieceSlots[index]
            val oldPiece = slot.removePiece()
            if (oldPiece != null) {
                returnPieceToPool(oldPiece)
            }
            val uiPiece = createUIPiece(piece)
            uiPiece ?: continue
            slot.placePiece(uiPiece)
        }
    }

    fun returnPieceToPool(piece: UIPiece) {
        val piecePool = getPiecePool(piece.pieceType)
        piecePool?.free(piece)
    }

    fun takePiece(piece: Piece): UIPiece? {
        for (slot in pieceSlots) {
            if (slot.piece?.actualPiece == piece) {
                return slot.removePiece()
            }
        }
        return null
    }

    fun createUIPiece(piece: Piece): UIPiece? {
        val piecePool = getPiecePool(piece)
        piecePool ?: return null
        val uiPiece = piecePool.obtain()
        uiPiece.setActualPiece(piece)
        return uiPiece
    }

    private fun getPiecePool(piece: Piece): Pool<UIPiece>? {
        val piecePool = piecePools.firstOrNull { it.first.isSamePieceType(piece) }
        return piecePool?.second
    }
}