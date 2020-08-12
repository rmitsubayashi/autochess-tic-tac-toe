package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.AttackRangeCalculator
import com.github.rmitsubayashi.game.Game

class DealEffectDamage(eventActor: Piece, private val timing: EventType,
                       private val target: EventActor, private val amount: Int)
    : Action(eventActor) {
    init {
        // in case of user input events, this should happen before the actual event
        // for example, when triggered on place piece, should wait to place piece first
        if (target is UnspecifiedTarget && target.type == UnspecifiedTarget.TargetType.SELECT_OPPONENT_PIECE) {
            priority = 1
        }
    }
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != timing) return false
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
        if (target is UnspecifiedTarget
                && target.type == UnspecifiedTarget.TargetType.SELECT_OPPONENT_PIECE
                && userInput == null
            ) {
            val enemyPlayer = game.getOpposingPlayer(player)
            val enemyPieces = game.board.filter { it?.player == enemyPlayer }.filterNotNull()
            game.waitForUserInput(this, event, enemyPieces)
            return emptyList()
        }
        val opposingPlayer = game.getOpposingPlayer(player)
        val actualTargets: List<Piece> = when (target) {
            is Piece -> listOf(target)
            is UnspecifiedTarget -> {
                when (target.type) {
                    UnspecifiedTarget.TargetType.RANDOM_OPPONENT -> {
                        val randomPiece =
                                game.board.filter { opposingPlayer?.id == it?.player?.id }.random()
                        if (randomPiece == null) emptyList() else listOf(randomPiece)
                    }
                    UnspecifiedTarget.TargetType.SELECT_OPPONENT_PIECE -> {
                        if (userInput !=null) { listOf(userInput) } else {
                            emptyList()}
                    }
                    UnspecifiedTarget.TargetType.ALL_OPPONENTS -> {
                        game.board.filter { opposingPlayer?.id == it?.player?.id }.filterNotNull()
                    }
                    UnspecifiedTarget.TargetType.ADJACENT_PIECES -> {
                        val pieceSquare = game.board.indexOf(piece)
                        val adjacentSquares = AttackRangeCalculator.getAdjacentSquares(pieceSquare)
                        adjacentSquares.mapNotNull { game.board[it] }
                    }
                    else -> emptyList()
                }
            }
            else -> emptyList()
        }
        for (t in actualTargets) {
            t.currHP -= amount
        }
        val damagedEvents = actualTargets.map {
            Event(EventType.pieceDamaged, it, null, mapOf(Pair(EventDataKey.AMOUNT, it.currHP)))
        }
        return damagedEvents.plus(
                Event(EventType.shouldAnimate, null, null)
        )
    }

    override fun copy(): Action {
        return DealEffectDamage(eventActor as Piece, timing, target, amount)
    }
}