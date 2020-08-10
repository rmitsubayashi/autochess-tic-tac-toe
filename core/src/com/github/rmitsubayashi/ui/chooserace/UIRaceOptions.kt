package com.github.rmitsubayashi.ui.chooserace

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Race
import com.github.rmitsubayashi.ui.util.UIClickListener
import com.github.rmitsubayashi.ui.util.appSkin

class UIRaceOptions: Table() {
    private var selection: Race? = null
    fun getSelection(): Race? {
        return selection
    }

    fun setOptions(races: List<Race>) {
        for (race in races) {
            val label = Label(race.name, appSkin)
            label.addListener(
                    UIClickListener(label,
                            {
                                selection = race
                            })
            )
            this.add(label)
        }
    }
}