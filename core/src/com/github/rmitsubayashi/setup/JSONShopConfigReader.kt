package com.github.rmitsubayashi.setup

import com.badlogic.gdx.Gdx
import com.github.rmitsubayashi.game.Shop
import com.github.rmitsubayashi.setup.entity.JSONShopConfig
import com.google.gson.Gson

class JSONShopConfigReader(private val fileName: String): ShopConfigReader {
    override fun read(): Shop.Configs {
        val fileContent = Gdx.files.internal(fileName).readString()
        val jsonShopConfig: JSONShopConfig = Gson().fromJson(fileContent, JSONShopConfig::class.java)
        return jsonShopConfig.toPiecePoolConfig()
    }
}