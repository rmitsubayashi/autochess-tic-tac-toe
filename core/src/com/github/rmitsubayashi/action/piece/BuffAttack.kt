package com.github.rmitsubayashi.action.piece

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.action.UnspecifiedTarget
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game

class BuffAttack(eventActor: Piece, private val amount: Int, private val timing: EventType)
    : Action(eventActor) {
    init {
        priority = 0
    }

    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != timing) return false
        if (eventActor != event.actor) return false
        if (event.actedUpon !is Piece && event.actedUpon !is UnspecifiedTarget) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val piece = event.actor as Piece
        if (event.actedUpon is UnspecifiedTarget
                && event.actedUpon.type == UnspecifiedTarget.TargetType.SELECT_ALLY_PIECE
                && userInput == null
        ) {
            val allyPieces = game.board.filter { it?.player == piece.player }.filterNotNull()
            game.waitForUserInput(this, event, allyPieces)
            return emptyList()
        }
        val targetPieces = when (event.actedUpon){
            is Piece -> listOf(event.actedUpon)
            is UnspecifiedTarget -> {
                when (event.actedUpon.type) {
                    UnspecifiedTarget.TargetType.ALL_ALLY_PIECES ->
                        game.board.filter { it?.player?.id == piece.player?.id }
                    UnspecifiedTarget.TargetType.SELECT_ALLY_PIECE ->
                        if (userInput == null) listOf(userInput) else emptyList()
                    else -> emptyList()
                }

            }
            else -> emptyList()
        }
        for (p in targetPieces) {
            p ?: continue
            p.currStats = p.currStats.copy(attack = p.currStats.attack + amount)
        }
        return emptyList()
    }

    override fun copy(): Action {
        return BuffAttack(eventActor as Piece, amount, timing)
    }
}
