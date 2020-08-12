package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Pool
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.game.Shop
import com.github.rmitsubayashi.ui.assets.ImageAssets
import com.github.rmitsubayashi.ui.assets.SoundAssets

class UIPiecePool(private val assetManager: AssetManager, private val game: Game) {
    // each piece has its own piece pool
    private val allPools = mutableListOf<Pair<Piece, Pool<UIPiece>>>()

    init {
        val shop = game.getShop(game.player1)
        if (shop != null) {
            populatePool(shop)
        }
    }

    private fun populatePool(shop: Shop) {
        // we want all pieces, not just the player's.
        // if there is a problem with memory, fix
        val allPieces = shop.getAllPieces()
        for (piece in allPieces) {
            addToPool(piece)
        }
    }

    private fun addToPool(piece: Piece){
        val pair = Pair(
                piece,
                object: Pool<UIPiece>() {
                    override fun newObject(): UIPiece = UIPiece.create(assetManager, piece, game)
                }
        )
        allPools.add(pair)
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
        var piecePool = getPiecePool(piece)
        if (piecePool == null) {
            if (!piece.isToken()) return null
            // tokens are not pre-populated, so dynamically create the pool
            loadTokenAssets(piece)
            addToPool(piece)
            piecePool = getPiecePool(piece)
        }
        val uiPiece = piecePool?.obtain()
        uiPiece?.setActualPiece(piece)
        return uiPiece
    }

    private fun loadTokenAssets(piece: Piece) {
        assetManager.load(SoundAssets.fromToken(piece))
        assetManager.load(ImageAssets.fromToken(piece))
        assetManager.finishLoading()
    }

    private fun getPiecePool(piece: Piece): Pool<UIPiece>? {
        val piecePool = allPools.firstOrNull { it.first.isSamePieceType(piece) }
        return piecePool?.second
    }
}