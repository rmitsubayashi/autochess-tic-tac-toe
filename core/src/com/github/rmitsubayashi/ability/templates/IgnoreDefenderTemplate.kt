package com.github.rmitsubayashi.ability.templates

import com.github.rmitsubayashi.ability.AbilityTemplate
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.piece.IgnoreDefender
import com.github.rmitsubayashi.entity.Piece

class IgnoreDefenderTemplate: AbilityTemplate {
    override fun getMatcher(): Regex {
        return Regex("^Ignores defenders$")
    }

    override fun parseAbility(piece: Piece, abilityDescription: String): Action? {
        return IgnoreDefender()
    }
}