package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.entity.Player

class GameJudge(private val winningScore: Int, private val player1: Player, private val player2: Player) {
    fun checkWinner(): GameState {
        if (player1.id == player2.id) {
            return GameState.Error
        }
        if (player1.score >= winningScore && player2.score >= winningScore) {
            return GameState.Tie
        }
        if (player1.score >= winningScore) {
            return GameState.Winner(player1)
        }
        if (player2.score >= winningScore) {
            return GameState.Winner(player2)
        }
        return GameState.InProgress
    }
}