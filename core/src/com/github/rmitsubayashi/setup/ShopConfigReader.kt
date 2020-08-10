package com.github.rmitsubayashi.setup

import com.github.rmitsubayashi.game.Shop

interface ShopConfigReader {
    fun read(): Shop.Configs
}