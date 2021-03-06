package com.github.rmitsubayashi.action.phase

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.action.piece.AttackFirst
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class BattlePhase(eventActor: Player): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.enterBattlePhase) return false
        if (event.actor != eventActor) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val playerPieces = game.board.filter { it?.player?.id == (eventActor as Player).id }.filterNotNull()
        val shuffledOrder = playerPieces.shuffled().toMutableList()
        val firstAttackers = shuffledOrder.filter { it.passiveActionExists(AttackFirst()) }
        for (firstAttacker in firstAttackers) {
            shuffledOrder.remove(firstAttacker)
            shuffledOrder.add(0, firstAttacker)
        }
        for (piece in shuffledOrder) {
            // each attack finishes before the next piece attack
            game.notifyEvent(Event(EventType.pieceDeclaresAttack, piece, null))
        }
        game.animationQueue.playQueuedAnimations {
            game.gameProgressManager.nextPlayerTurn()
        }
        return emptyList()
    }

    override fun copy(): Action {
        return BattlePhase(eventActor as Player)
    }
}