package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game

class SpawnToken(eventActor: Piece, private val tokenTemplate: Piece, private val timing: EventType, private val target: UnspecifiedTarget): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != timing) return false
        if (timing == EventType.placePiece && event.isDone()) return false
        if (event.actedUpon != eventActor) return false
        return true

    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val piece = event.actedUpon as Piece
        val newPiece = tokenTemplate.copy()
        newPiece.player = piece.player
        val square = when (target.type) {
            UnspecifiedTarget.TargetType.RANDOM_SQUARE -> {
                val markEmptySquares = game.board.mapIndexed {
                    index, piece ->
                    if (piece == null) {
                        index
                    } else {
                        -1
                    }
                }
                val emptySquareIndexes = markEmptySquares.filter { it != -1 }
                if (emptySquareIndexes.isEmpty()) {
                    -1
                } else {
                    emptySquareIndexes.random()
                }
            } else -> {
                -1
            }
        }
        if (square == -1) return emptyList()

        return listOf(
                Event(EventType.placePiece, piece.player, newPiece,
                mapOf(Pair(EventDataKey.SQUARE, square)))
        )
    }

    override fun copy(): Action {
        return SpawnToken(eventActor as Piece, tokenTemplate, timing, target)
    }
}