package com.github.rmitsubayashi.ability

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.entity.Piece

interface AbilityTemplate {
    fun getMatcher(): Regex
    fun parseAbility(piece: Piece, abilityDescription: String): List<Action>
}