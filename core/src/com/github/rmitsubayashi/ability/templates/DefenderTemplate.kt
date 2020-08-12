package com.github.rmitsubayashi.ability.templates

import com.github.rmitsubayashi.ability.AbilityTemplate
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.piece.Defender
import com.github.rmitsubayashi.entity.Piece

class DefenderTemplate: AbilityTemplate {
    override fun getMatcher(): Regex {
        return Regex("^Defender$")
    }

    override fun parseAbility(piece: Piece, abilityDescription: String): Action {
        return Defender()
    }
}