package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.entity.Player
import kotlin.math.min

object MoneyCalculator {
    fun calculateStartOfTurnMoney(player: Player): Int {
        return min(player.level, 5)
    }
}