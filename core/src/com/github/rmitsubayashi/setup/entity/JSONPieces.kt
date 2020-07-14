package com.github.rmitsubayashi.setup.entity

import com.github.rmitsubayashi.ability.AbilityList
import com.google.gson.annotations.SerializedName
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.setup.entity.JSONPiece

data class JSONPieces(
        @SerializedName("pieces")
        val pieces: List<JSONPiece>
) {
        fun toPieces(abilityList: AbilityList): List<Piece> =
                pieces.map { it.toPiece(abilityList) }
}