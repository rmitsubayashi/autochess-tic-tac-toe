package com.github.rmitsubayashi.setup

import com.github.rmitsubayashi.ability.AbilityList
import com.github.rmitsubayashi.entity.Piece

interface PieceFileReader {
    fun read(abilityList: AbilityList): List<Piece>
}