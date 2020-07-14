package com.github.rmitsubayashi.ability

import com.github.rmitsubayashi.ability.templates.DealDamage
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.entity.Piece

class AbilityList {
    // TODO try doing this dynamically
    // https://github.com/google/guava/blob/master/android/guava/src/com/google/common/reflect/ClassPath.java
    private val list: MutableList<AbilityTemplate> = mutableListOf()

    init {
        list.apply {
            add(DealDamage())
        }
    }

    fun mapAbilityToActions(abilityDescription: String, piece: Piece): List<Action> {
        for (template in list){
            if (template.getMatcher().matches(abilityDescription)) {
                return template.parseAbility(piece, abilityDescription)
            }
        }
        return emptyList()
    }

}