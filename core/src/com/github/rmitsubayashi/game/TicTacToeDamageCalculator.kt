package com.github.rmitsubayashi.game

import kotlin.math.ceil

object TicTacToeDamageCalculator {
    fun getDamage(turns: Int): Int {
        return ceil(turns / 4.0).toInt()
    }
}