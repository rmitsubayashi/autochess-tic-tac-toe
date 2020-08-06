package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.EmptyEventActor
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game

class Damaged: Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.pieceDamaged) return false
        if (event.actor !is Piece) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val piece = event.actor as Piece
        if (piece.isDead()) {
            game.board.removePiece(piece)
            game.piecePool.putBackInPool(piece)
            game.actionObservable.unsubscribeActions(piece.actions)
            return emptyList()
        }
        return emptyList()
    }

    override fun copy(): Action {
        return Damaged()
    }
}