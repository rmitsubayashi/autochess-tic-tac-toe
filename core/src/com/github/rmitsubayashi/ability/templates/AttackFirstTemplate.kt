package com.github.rmitsubayashi.ability.templates

import com.github.rmitsubayashi.ability.AbilityTemplate
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.piece.AttackFirst
import com.github.rmitsubayashi.entity.Piece

class AttackFirstTemplate: AbilityTemplate {
    override fun getMatcher(): Regex {
        val regex = "Attacks first"
        return Regex(regex)
    }

    override fun parseAbility(piece: Piece, abilityDescription: String): Action? {
        return AttackFirst()
    }
}