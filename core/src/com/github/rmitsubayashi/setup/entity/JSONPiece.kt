package com.github.rmitsubayashi.setup.entity

import com.github.rmitsubayashi.ability.AbilityList
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Race
import com.github.rmitsubayashi.entity.Stats
import com.google.gson.annotations.SerializedName

data class JSONPiece(
    @SerializedName("name")
    val name: String,
    @SerializedName("race")
    val race: String,
    @SerializedName("hp")
    val hp: Int,
    @SerializedName("attack")
    val attack: Int,
    @SerializedName("attackRange")
    val attackRange: String,
    @SerializedName("ability")
    val ability: String,
    @SerializedName("cost")
    val cost: Int
) {
    fun toPiece(abilityList: AbilityList): Piece {
        val tempList = mutableListOf<Action>()
        val piece = Piece(
                name = name,
                race = (Race.fromString(race)),
                stats = Stats(hp, attack),
                ability = ability,
                attackRange = AttackRangeMapper.fromString(attackRange),
                cost = cost,
                actions = tempList,
                player = null
        )
        tempList.addAll(abilityList.mapAbilityToActions(ability, piece))
        return piece
    }
}