package com.github.rmitsubayashi.ui.chooserace

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.rmitsubayashi.GdxGame
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.ai.AIChooseRace
import com.github.rmitsubayashi.ai.CrudeAIChooseRace
import com.github.rmitsubayashi.ui.game.GameScreen
import com.github.rmitsubayashi.ui.util.IStageScreen
import com.github.rmitsubayashi.ui.util.UIClickListener
import com.github.rmitsubayashi.ui.util.appSkin
import com.github.rmitsubayashi.ui.util.setBackgroundColor

class ChooseRaceScreen(private val game: GdxGame): IStageScreen(game) {
    private val options = UIRaceOptions()
    private val submitButton = TextButton("Start", appSkin)

    init {
        submitButton.addListener(
                UIClickListener(
                        submitButton,
                        {
                            val option = options.getSelection()
                            option ?: return@UIClickListener
                            game.game.notifyEvent(
                                    Event(EventType.CHOOSE_RACE, game.game.player1,null,
                                            mapOf(Pair(EventDataKey.RACES, option), Pair(EventDataKey.DONE, true)))
                            )
                        }
                )
        )
        val table = Table()
        table.setFillParent(true)
        table.setBackgroundColor(appSkin.getColor("white"))
        val instructionsLabel = Label("Choose a starting race", appSkin.get("big", Label.LabelStyle::class.java))
        table.add(instructionsLabel).padBottom(24f).row()
        table.add(options).padBottom(10f).row()
        table.add(submitButton)
        this.stage.addActor(table)

        subscribeAction(RaceChosen(this))
        subscribeAction(ShowRaceOptions(game.game.player1, options))
        subscribeAction(AIChooseRace(game.game.player2, CrudeAIChooseRace()))

        chooseRaceOptions()
    }

    fun gotToGameScreen() {
        game.setScreen(GameScreen(game))
        dispose()
    }

    private fun chooseRaceOptions() {
        val p1RaceOptions = game.game.raceSelector.getRaceOptions(2)
        val p2RaceOptions = game.game.raceSelector.getRaceOptions(2)
        game.game.notifyEvent(
                Event(EventType.CHOOSE_RACE, game.game.player1, null, mapOf(Pair(EventDataKey.RACES, p1RaceOptions)))
        )
        game.game.notifyEvent(
                Event(EventType.CHOOSE_RACE, game.game.player2, null, mapOf(Pair(EventDataKey.RACES, p2RaceOptions)))
        )
    }
}