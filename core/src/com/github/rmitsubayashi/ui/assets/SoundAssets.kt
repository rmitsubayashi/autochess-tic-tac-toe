package com.github.rmitsubayashi.ui.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Sound
import com.github.rmitsubayashi.entity.Piece
import java.util.*

object SoundAssets {
    private const val BASE_PATH = "sound"
    // ogg files are a standard in game dev so enforce here
    private const val DEFAULT_FORMAT = "ogg"
    val attack = AssetDescriptor<Sound>("$BASE_PATH/attack.$DEFAULT_FORMAT", Sound::class.java)
    fun fromPiece(piece: Piece): AssetDescriptor<Sound> {
        val path = piece.name
                .toLowerCase(Locale.ROOT)
                .replace(' ', '_')

        return AssetDescriptor("sound/pieces/$path.$DEFAULT_FORMAT", Sound::class.java)
    }
}