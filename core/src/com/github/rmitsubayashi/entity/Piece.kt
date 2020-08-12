package com.github.rmitsubayashi.entity

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.EventActor
import com.github.rmitsubayashi.action.piece.PassiveAction

class Piece(
    val name: String,
    val race: Race,
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
    private var isToken = false

    fun copy(): Piece {
        val isTokenCopy = isToken
        // make sure the actions are mapped to the new piece
        val actionsCopy = actions.map { a -> a.copy() }
        return Piece(
                name, race, stats, ability, attackRange, cost, actionsCopy, player
        ).apply {
            this.actions.forEach { it.eventActor = this }
            this.isToken = isTokenCopy
        }
    }

    fun isSamePieceType(other: Piece): Boolean =
            name == other.name &&
            race == other.race &&
            stats == other.stats &&
            ability == other.ability &&
            cost == other.cost &&
            isToken == other.isToken

    fun isDead(): Boolean = currHP <= 0

    fun passiveActionExists(passiveAction: PassiveAction): Boolean {
        return actions.firstOrNull { it::class == passiveAction::class } != null
    }

    fun toToken() {
        isToken = true
    }

    fun isToken(): Boolean = isToken
}