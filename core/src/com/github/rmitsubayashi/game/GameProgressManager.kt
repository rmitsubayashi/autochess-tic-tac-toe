package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Player

class GameProgressManager(
        private val game: Game
) {
    val currPlayer: Player get() = _currPlayer
    private var _currPlayer = game.player1
    private var nextPlayer = game.player2
    val turn: Int get() = _turn
    private var _turn: Int = 1
    val phase: Phase get() = _phase
    private var _phase: Phase = Phase.SETUP


    fun toBattlePhase() {
        game.notifyEvent(
                Event(EventType.enterBattlePhase, currPlayer, null)
        )
        _phase = Phase.BATTLE
    }

    fun nextPlayerTurn() {
        //switch player
        val temp = nextPlayer
        nextPlayer = currPlayer
        _currPlayer = temp
        //next turn
        _turn ++
        game.notifyEvent(
                Event(EventType.enterSetupPhase, currPlayer, null)
        )
        _phase = Phase.SETUP
    }

    enum class Phase {
        SETUP,
        BATTLE
    }
}