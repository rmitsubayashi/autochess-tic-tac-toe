package com.github.rmitsubayashi.entity

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.EventActor

class Piece(
    val name: String,
    val stats: Stats,
    val ability: String,
    val attackRange: AttackRange,
    val cost: Int,
    val actions: List<Action>,
    var player: Player? = null
): EventActor {
    // the hp here is max hp
    var currStats = stats.copy()
    var currHP = stats.hp

    fun copy(): Piece {
        // make sure the actions are mapped to the new piece
        val actionsCopy = actions.map { a -> a.copy() }
        return Piece(
                name, stats, ability, attackRange, cost, actionsCopy, player
        ).apply {
            this.actions.forEach { it.eventActor = this }
        }
    }

    fun isSamePieceType(other: Piece): Boolean =
            name == other.name &&
            stats == other.stats &&
            ability == other.ability &&
            cost == other.cost

    fun isDead(): Boolean = currHP <= 0
}