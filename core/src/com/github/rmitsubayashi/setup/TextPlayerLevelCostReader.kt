package com.github.rmitsubayashi.setup

import com.badlogic.gdx.Gdx

class TextPlayerLevelCostReader(private val fileName: String): PlayerLevelCostReader {
    override fun read(): Map<Int, Int> {
        val fileContent = Gdx.files.internal(fileName).readString()
        val costs = fileContent.split(',')
        val tempMap = mutableMapOf<Int, Int>()
        for ((index, cost) in costs.withIndex()) {
            tempMap[index] = cost.toInt()
        }
        return tempMap
    }
}