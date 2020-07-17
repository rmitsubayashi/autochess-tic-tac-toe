package com.github.rmitsubayashi.ai

import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

interface GameAI {
    fun execute(game: Game, player: Player)
}