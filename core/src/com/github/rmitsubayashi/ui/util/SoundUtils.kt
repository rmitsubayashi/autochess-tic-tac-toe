package com.github.rmitsubayashi.ui.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.github.rmitsubayashi.entity.Piece
import java.util.*

object SoundUtils {
    fun getSound(piece: Piece): Sound {
        val path = pieceNameToSoundFilePath(piece.name)
        return Gdx.audio.newSound(Gdx.files.internal(path))
    }

    private fun pieceNameToSoundFilePath(pieceName: String): String {
        val path = pieceName
                .toLowerCase(Locale.ROOT)
                .replace(' ', '_')
        // ogg files are a standard in game dev
        return "sound/pieces/$path.ogg"
    }

    fun playSound(sound: Sound) {

    }
}