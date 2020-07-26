package com.github.rmitsubayashi.ai

import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class CrudeAIUserInput: GameAI {
    override fun execute(game: Game, player: Player) {
        val randomTarget = game.userInputManager.possibleTargets.random()
        game.userInputManager.handleActionWaitingForUserInput(randomTarget)
    }
}