package com.github.rmitsubayashi.ui.game.action

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.GdxGame
import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.AnimationConfig
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.results.ResultsScreen

class ShowResultScreen(private val game: GdxGame, private val player: Player)
    : Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.playerWins) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        game.animationQueue.addAnimation(
                AnimationConfig(
                        Actions.delay(0.1f),
                        null,
                        0.1f
                ) {
                    this.game.setScreen(ResultsScreen(this.game, player))
                }
        )
        return emptyList()
    }

    override fun copy(): Action {
        return ShowResultScreen(game, player)
    }
}