package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.entity.Race

class RaceSelector {
    private val allRaces: MutableList<Race> = Race.values().toMutableList()

    fun getRaceOptions(ct: Int): List<Race> {
        var optionsToFill = ct
        if (allRaces.size < optionsToFill) {
            return allRaces.toList()
        }
        val options = mutableSetOf<Race>()
        while (optionsToFill > 0) {
            val randomRace = allRaces.random()
            if (!options.contains(randomRace)) {
                options.add(randomRace)
                optionsToFill--
            }
        }
        return options.toList()
    }

    fun markRaceSelected(race: Race) {
        allRaces.remove(race)
    }
}