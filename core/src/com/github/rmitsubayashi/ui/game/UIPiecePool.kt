package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Pool
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.game.Shop

class UIPiecePool(private val assetManager: AssetManager, private val game: Game) {
    // each piece has its own piece pool
    private lateinit var allPools: List<Pair<Piece, Pool<UIPiece>>>

    init {
        val shop = game.getShop(game.player1)
        if (shop != null) {
            populatePool(shop)
        }
    }

    private fun populatePool(shop: Shop) {
        // we want all pieces, not
        val allPieces = shop.getAllPieces()
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
        allPools = tempList
    }

    fun returnPieceToPool(piece: UIPiece) {
        val piecePool = getPiecePool(piece.pieceType)
        piecePool?.free(piece)
    }

    fun returnPieceToPool(pieces: List<UIPiece>) {
        for (p in pieces) {
            returnPieceToPool(p)
        }
    }

    fun createUIPiece(piece: Piece): UIPiece? {
        val piecePool = getPiecePool(piece)
        piecePool ?: return null
        val uiPiece = piecePool.obtain()
        uiPiece.setActualPiece(piece)
        return uiPiece
    }

    private fun getPiecePool(piece: Piece): Pool<UIPiece>? {
        val piecePool = allPools.firstOrNull { it.first.isSamePieceType(piece) }
        return piecePool?.second
    }
}