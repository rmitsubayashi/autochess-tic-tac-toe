package com.github.rmitsubayashi.ui.chooserace

import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.entity.Race
import com.github.rmitsubayashi.game.Game

class RaceChosen(private val chooseRaceScreen: ChooseRaceScreen)
    : Action(EmptyEventActor()) {
    private var player1Chosen = false
    private var player2Chosen = false

    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.CHOOSE_RACE) return false
        if (event.data?.get(EventDataKey.DONE) != true) return false
        if (event.data[EventDataKey.RACES] !is Race) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        val race = event.data?.get(EventDataKey.RACES) as Race
        if (player == game.player1) {
            player1Chosen = true
            game.getShop(player)?.addRace(race)
        }
        if (player == game.player2) {
            player2Chosen = true
            game.getShop(player)?.addRace(race)
        }
        game.raceSelector.markRaceSelected(race)
        if (player1Chosen && player2Chosen) {
            chooseRaceScreen.gotToGameScreen()
        }
        return emptyList()
    }

    override fun copy(): Action {
        return RaceChosen(chooseRaceScreen)
    }
}