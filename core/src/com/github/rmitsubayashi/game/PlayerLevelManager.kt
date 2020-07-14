package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.entity.Player

class PlayerLevelManager(private val levelUpTable: Map<Int,Int>) {
    fun getLevelUpCost(level: Int): Int = levelUpTable[level] ?: 0
    fun hasReachedMaxLevel(player: Player) = !levelUpTable.containsKey(player.level+1)
}