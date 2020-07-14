package com.github.rmitsubayashi.setup.entity

import com.github.rmitsubayashi.entity.AttackRange

object AttackRangeMapper {
    fun fromString(attackRangeString: String): AttackRange =
            when (attackRangeString) {
                "melee" -> AttackRange.melee
                "ranged" -> AttackRange.ranged
                else -> throw Exception("cannot parse $attackRangeString to attack range")
            }
}