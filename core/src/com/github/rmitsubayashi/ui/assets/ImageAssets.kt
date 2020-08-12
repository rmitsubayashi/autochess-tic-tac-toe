package com.github.rmitsubayashi.ui.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.github.rmitsubayashi.entity.Piece
import java.util.*

object ImageAssets {
    private const val BASE_PATH = "image"
    val field = AssetDescriptor<Texture>("$BASE_PATH/grassfield.jpg", Texture::class.java)
    val ticTacToe = AssetDescriptor<Texture>("$BASE_PATH/tictactoe.png", Texture::class.java)
    val shield = AssetDescriptor<Texture>("$BASE_PATH/shield.png", Texture::class.java)
    val bolt = AssetDescriptor<Texture>("$BASE_PATH/bolt.png", Texture::class.java)
    fun fromPiece(piece: Piece): AssetDescriptor<Texture> {
        val path = piece.name
                .toLowerCase(Locale.ROOT)
                .replace(' ', '_')
        return AssetDescriptor("$BASE_PATH/pieces/$path.png", Texture::class.java)
    }
    fun fromToken(piece: Piece): AssetDescriptor<Texture> {
        val path = (piece.name + "_token")
                .toLowerCase(Locale.ROOT)
                .replace(' ', '_')

        return AssetDescriptor("$BASE_PATH/pieces/$path.png", Texture::class.java)
    }
}