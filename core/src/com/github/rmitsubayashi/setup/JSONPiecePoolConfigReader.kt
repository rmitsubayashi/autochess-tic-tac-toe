package com.github.rmitsubayashi.setup

import com.badlogic.gdx.Gdx
import com.github.rmitsubayashi.game.PiecePool
import com.github.rmitsubayashi.setup.entity.JSONPiecePoolConfig
import com.google.gson.Gson

class JSONPiecePoolConfigReader(private val fileName: String): PiecePoolConfigReader {
    override fun read(): PiecePool.Configs {
        val fileContent = Gdx.files.internal(fileName).readString()
        val jsonPiecePoolConfig: JSONPiecePoolConfig = Gson().fromJson(fileContent, JSONPiecePoolConfig::class.java)
        return jsonPiecePoolConfig.toPiecePoolConfig()
    }
}