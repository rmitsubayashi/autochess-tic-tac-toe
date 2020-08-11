package com.github.rmitsubayashi.ui.game.action

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.AnimationConfig
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIHUD
import com.github.rmitsubayashi.ui.game.UIPhaseDisplay

class ShowPhaseDisplay(private val uiPhaseDisplay: UIPhaseDisplay, private val uihud: UIHUD): Action(EmptyEventActor()) {
    init {
        // want to call this before the action for the setup phase is called
        priority = 1
    }
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type !in listOf(EventType.ENTER_SECURE_PHASE, EventType.enterBattlePhase)) return false
        if (event.data?.get(EventDataKey.DONE) == true) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        if (event.type == EventType.ENTER_SECURE_PHASE) {
            uiPhaseDisplay.setTurnDisplay(
                    game.gameProgressManager.currPlayer
            )
        } else {
            uiPhaseDisplay.setBattlePhaseText()
        }

        val height = 400f-uiPhaseDisplay.height/2
        game.animationQueue.addAnimation(
                AnimationConfig(
                        Actions.sequence(
                            Actions.moveTo(480f, height),
                            Actions.show(),
                            Actions.moveTo(240f-300f/2, height, 0.5f, Interpolation.fastSlow),
                            Actions.delay(0.4f),
                            Actions.moveTo(-uiPhaseDisplay.width, height, 0.5f, Interpolation.slowFast),
                            Actions.hide()
                        ),
                        uiPhaseDisplay,
                        1.4f
                ) {
                    if (event.type == EventType.ENTER_SECURE_PHASE) uihud.updateTurn()
                }
        )

        return emptyList()
    }

    override fun copy(): Action {
        return ShowPhaseDisplay(uiPhaseDisplay, uihud)
    }
}