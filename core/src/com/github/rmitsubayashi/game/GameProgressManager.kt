package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Player

class GameProgressManager(
        private val game: Game
) {
    val currPlayer: Player get() = _currPlayer
    private var _currPlayer = game.player2
    private var nextPlayer = game.player1
    val turn: Int get() = _turn
    private var _turn: Int = 1
    val phase: Phase get() = _phase
    private var _phase: Phase = Phase.SETUP


    fun toBattlePhase() {
        if (_phase == Phase.BATTLE) return
        _phase = Phase.BATTLE
        game.notifyEvent(
                Event(EventType.enterBattlePhase, currPlayer, null)
        )
    }

    fun startGame(startingPlayer: Player) {
        if (_currPlayer != startingPlayer) {
            nextPlayer = _currPlayer
            _currPlayer = startingPlayer
        }
        _phase = Phase.SETUP
        game.notifyEvent(
                Event(EventType.enterSetupPhase, currPlayer, null)
        )
    }

    fun nextPlayerTurn() {
        if (_phase == Phase.SETUP) return
        //switch player
        val temp = nextPlayer
        nextPlayer = currPlayer
        _currPlayer = temp
        //next turn
        _turn ++
        _phase = Phase.SETUP
        game.notifyEvent(
                Event(EventType.enterSetupPhase, currPlayer, null)
        )
    }

    enum class Phase {
        SETUP,
        BATTLE
    }
}