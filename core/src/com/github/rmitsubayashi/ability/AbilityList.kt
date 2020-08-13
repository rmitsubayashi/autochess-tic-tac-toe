package com.github.rmitsubayashi.ability

import com.github.rmitsubayashi.ability.templates.*
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.entity.Piece

class AbilityList {
    // TODO try doing this dynamically
    // https://github.com/google/guava/blob/master/android/guava/src/com/google/common/reflect/ClassPath.java
    private val list: MutableList<AbilityTemplate> = mutableListOf()

    init {
        list.apply {
            add(DealDamageTemplate())
            add(DefenderTemplate())
            add(IgnoreDefenderTemplate())
            add(SpawnTokenTemplate())
            add(MoveTemplate())
        }
    }

    fun mapAbilityToAction(abilityDescription: String, piece: Piece): Action? {
        for (template in list){
            if (template.getMatcher().matches(abilityDescription)) {
                return template.parseAbility(piece, abilityDescription)
            }
        }
        return null
    }

}