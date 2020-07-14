package com.github.rmitsubayashi.setup

import com.github.rmitsubayashi.game.PiecePool

interface PiecePoolConfigReader {
    fun read(): PiecePool.Configs
}