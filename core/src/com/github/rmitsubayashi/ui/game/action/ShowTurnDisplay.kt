package com.github.rmitsubayashi.ui.game.action

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.AnimationConfig
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UITurnDisplay

class ShowTurnDisplay(private val uiTurnDisplay: UITurnDisplay): Action(EmptyEventActor()) {
    init {
        // want to call this before the action for the setup phase is called
        priority = 1
    }
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.enterSetupPhase) return false
        if (event.data?.get(EventDataKey.DONE) == true) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        uiTurnDisplay.setDisplay(
                game.gameProgressManager.turn,
                game.gameProgressManager.currPlayer
        )

        game.animationQueue.addAnimation(
                AnimationConfig(
                        Actions.fadeIn(1.5f),
                        uiTurnDisplay,
                        1.5f
                )
        )
        game.animationQueue.addAnimation(
                AnimationConfig(
                        Actions.fadeOut(1.5f),
                        uiTurnDisplay,
                        1.5f
                )
        )

        return emptyList()
    }

    override fun copy(): Action {
        return ShowTurnDisplay(uiTurnDisplay)
    }
}