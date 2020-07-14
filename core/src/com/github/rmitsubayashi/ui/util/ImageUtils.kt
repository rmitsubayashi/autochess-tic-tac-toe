package com.github.rmitsubayashi.ui.util

import com.github.rmitsubayashi.entity.Piece
import java.util.*

object ImageUtils {
    fun getImagePath(piece: Piece): String {
        val path = piece.name
                .toLowerCase(Locale.ROOT)
                .replace(' ', '_')
        return "pieces/$path.png"
    }
}