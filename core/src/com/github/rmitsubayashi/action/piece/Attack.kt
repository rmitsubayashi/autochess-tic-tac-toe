package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game

class Attack: Action(EmptyEventActor()) {

    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.pieceAttacks) { return false }
        if (event.actor !is Piece) { return false }
        if (event.actedUpon !is Piece) { return false }
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val piece = event.actor as Piece
        val enemyPiece = event.actedUpon as Piece
        enemyPiece.currHP = enemyPiece.currHP - piece.currStats.attack
        val enemyDamagedEvent = Event(EventType.pieceDamaged, enemyPiece, null,
            mapOf(Pair(EventDataKey.AMOUNT, enemyPiece.currHP)))
        piece.currHP = piece.currHP - enemyPiece.currStats.attack
        val pieceDamagedEvent = Event(EventType.pieceDamaged, piece, null,
            mapOf(Pair(EventDataKey.AMOUNT, piece.currHP)))
        return listOf(enemyDamagedEvent, pieceDamagedEvent)
    }

    override fun copy(): Attack {
        return Attack()
    }
}