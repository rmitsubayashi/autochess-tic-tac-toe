package com.github.rmitsubayashi.setup

import com.badlogic.gdx.Gdx
import com.github.rmitsubayashi.ability.AbilityList
import com.google.gson.Gson
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.setup.entity.JSONPieces

class JSONPieceFileReader(private val fileName: String): PieceFileReader {
    override fun read(abilityList: AbilityList): List<Piece> {
        val fileContent = Gdx.files.internal(fileName).readString()
        val jsonPieces = Gson().fromJson(fileContent, JSONPieces::class.java)
        return jsonPieces.toPieces(abilityList)
    }
}