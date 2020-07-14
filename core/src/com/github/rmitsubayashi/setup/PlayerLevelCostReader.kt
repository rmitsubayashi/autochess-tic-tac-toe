package com.github.rmitsubayashi.setup

interface PlayerLevelCostReader {
    //Map<level, cost>
    fun read(): Map<Int, Int>
}