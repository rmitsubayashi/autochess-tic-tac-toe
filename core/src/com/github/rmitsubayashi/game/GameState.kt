package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.entity.Player

sealed class GameState {
    object InProgress: GameState()
    class Winner(val winner: Player): GameState()
    object Tie: GameState()
    object Error: GameState()
}