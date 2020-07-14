package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game

class DealEffectDamage(eventActor: EventActor, private val timing: EventType,
                       private val target: EventActor, private val amount: Int)
    : Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type !in listOf(EventType.placePiece)) return false
        if (event.actor !is Piece) return false
        if (event.actor != eventActor) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        if (target is UnspecifiedTarget
                && target.type == UnspecifiedTarget.TargetType.SELECT_OPPONENT_PIECE
                && userInputResult == null
            ) {
            game.waitForUserInput(this)
            return emptyList()
        }
        val piece = event.actor as Piece
        val player = piece.player
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
                        userInputResult?.map { it as Piece } ?: emptyList()
                    }
                    UnspecifiedTarget.TargetType.ALL_OPPONENTS -> {
                        game.board.filter { opposingPlayer?.id == it?.player?.id }.filterNotNull()
                    }
                    else -> emptyList()
                }
            }
            else -> emptyList()
        }
        for (t in actualTargets) {
            t.currHP -= amount
        }
        return actualTargets.map {
            Event(EventType.pieceDamaged, it, null)
        }
    }

    override fun copy(): Action {
        return DealEffectDamage(eventActor, timing, target, amount)
    }
}