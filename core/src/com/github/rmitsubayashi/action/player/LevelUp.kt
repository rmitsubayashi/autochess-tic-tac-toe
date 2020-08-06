package com.github.rmitsubayashi.action.player

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game

class LevelUp(eventActor: Player): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.levelUp) return false
        if (event.actor !is Player) return false
        if (eventActor != event.actor) return false
        if (game.playerLevelManager.hasReachedMaxLevel(event.actor)) return false
        // doesn't have enough money to level up
        if (event.actor.money < game.playerLevelManager.getLevelUpCost(event.actor.level+1)){
            return false
        }
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        val cost = game.playerLevelManager.getLevelUpCost(player.level+1)
        player.money -= cost
        player.level += 1
        return listOf(
                Event(EventType.moneyChanged, event.actor, null)
        )
    }

    override fun copy(): Action {
        return LevelUp(eventActor as Player)
    }
}