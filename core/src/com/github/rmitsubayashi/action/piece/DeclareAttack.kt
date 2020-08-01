package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.EmptyEventActor
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.AttackRangeCalculator
import com.github.rmitsubayashi.game.Game

class DeclareAttack: Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.pieceDeclaresAttack) { return false }
        if (event.actor !is Piece) { return false }
        if (event.actor.isDead()) { return false }
        if (event.actor.currStats.attack == 0) { return false }
        if (AttackRangeCalculator.getPossibleAttackTargets(game.board, event.actor).isEmpty()) {
            return false
        }
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val piece = event.actor as Piece
        val possibleTargets = AttackRangeCalculator.getPossibleAttackTargets(game.board, piece)
        val target = possibleTargets.random()
        return listOf(Event(EventType.pieceAttacks, piece, target))
    }

    override fun copy(): Action {
        return DeclareAttack()
    }
}