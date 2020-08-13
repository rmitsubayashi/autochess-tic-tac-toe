package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game

// only one selection allowed (for ux reasons).
// we cannot choose a piece AND choose a square to move it to
class Move(eventActor: Piece, private val timing: EventType, private val target: UnspecifiedTarget, private val squareTarget: UnspecifiedTarget): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != timing) return false
        if (timing == EventType.placePiece && event.isDone()) return false
        if (timing == EventType.placePiece && event.data?.get(EventDataKey.SQUARE) !is Int) return false
        if (event.actedUpon !is Piece) return false
        if (event.actedUpon != eventActor) return false
        val enemyPlayer = game.getOpposingPlayer(event.actedUpon.player)
        val enemyPieces = game.board.filter { it?.player == enemyPlayer }
        if (enemyPieces.isEmpty()) {
            return false
        }
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val piece = event.actedUpon as Piece
        val player = piece.player
        if (target.type == UnspecifiedTarget.TargetType.SELECT_OPPONENT_PIECE
                && userInput == null
        ) {
            val enemyPlayer = game.getOpposingPlayer(player)
            val enemyPieces = game.board.filter { it?.player == enemyPlayer }.filterNotNull()
            game.waitForUserInput(this, event, enemyPieces)
            return emptyList()
        }
        val actualTargets: List<Piece> = when (target.type) {
            UnspecifiedTarget.TargetType.SELECT_OPPONENT_PIECE -> {
                if (userInput != null) { listOf(userInput) } else { emptyList() }
            }
            UnspecifiedTarget.TargetType.ALL_PIECES -> {
                game.board.filter { it != null && it != piece }.filterNotNull()
            }
            else -> emptyList()
        }

        // we should manually move pieces, not add an event for it
        // because adding the event will trigger abilities
        val newEvents = mutableListOf<Event>()
        val pieceSquare = if (timing == EventType.placePiece) {
            event.data?.get(EventDataKey.SQUARE) as Int
        } else {
            -1
        }
        if (actualTargets.size == 1) {
            if (squareTarget.type == UnspecifiedTarget.TargetType.RANDOM_SQUARE) {
                val markEmptySquares = game.board.mapIndexed {
                    index, square ->
                    if (square == null) {
                        index
                    } else {
                        -1
                    }
                }
                val emptySquareIndexes = markEmptySquares
                        .filter { it != -1 }
                        .filter { it != pieceSquare }
                if (emptySquareIndexes.isNotEmpty()) {
                    val actualTargetSquare = game.board.indexOf(actualTargets[0])
                    val randomSquare = emptySquareIndexes.random()
                    game.board[randomSquare] = actualTargets[0]
                    game.board[actualTargetSquare] = null
                    newEvents.add(
                            Event(EventType.placePiece, actualTargets[0], null,
                                    mapOf(Pair(EventDataKey.SQUARE, randomSquare), Pair(EventDataKey.DONE, true)))
                    )
                }
            }
        } else {
            if (squareTarget.type == UnspecifiedTarget.TargetType.RANDOM_SQUARE) {
                val availableSquares = game.board
                        .mapIndexed { index, _ -> index }
                        .filter { it != pieceSquare }
                        .toMutableList()
                for ((index, square) in game.board.withIndex()) {
                    if (square != piece) {
                        game.board[index] = null
                    }
                }
                for (actualTarget in actualTargets) {
                    val randomSquare = availableSquares.random()
                    game.board[randomSquare] = actualTarget
                    newEvents.add(
                            Event(EventType.placePiece, actualTarget, null,
                            mapOf(Pair(EventDataKey.SQUARE, randomSquare), Pair(EventDataKey.DONE, true)))
                    )
                    availableSquares.remove(randomSquare)
                }
            }
        }

        return newEvents
    }

    override fun copy(): Action {
        return Move(eventActor as Piece, timing, target, squareTarget)
    }
}