package com.github.rmitsubayashi.action.phase

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventActor
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class BattlePhase(eventActor: EventActor): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        return event.type == EventType.enterBattlePhase && event.actor == eventActor
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        val playerPieces = game.board.filter { it?.player?.id == (eventActor as Player).id }
        for (piece in playerPieces) {
            // each attack finishes before the next piece attack
            game.notifyEvent(Event(EventType.pieceDeclaresAttack, piece, null))
        }
        game.animationQueue.playQueuedAnimations {
            game.gameProgressManager.nextPlayerTurn()
        }
        return emptyList()
    }

    override fun copy(): Action {
        return BattlePhase(eventActor)
    }
}