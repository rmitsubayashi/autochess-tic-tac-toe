package com.github.rmitsubayashi.ui.chooserace

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.rmitsubayashi.entity.Race
import com.github.rmitsubayashi.ui.util.UIClickListener
import com.github.rmitsubayashi.ui.util.appSkin

class UIRaceOptions: Table() {
    private var selection: Race? = null
    private val allOptions = mutableListOf<TextButton>()
    fun getSelection(): Race? {
        return selection
    }

    fun setOptions(races: List<Race>) {
        val borderlessTextButtonStyle = TextButton.TextButtonStyle(appSkin.get("borderless", TextButton.TextButtonStyle::class.java))
        borderlessTextButtonStyle.checkedFontColor = appSkin.getColor("gray")
        for (race in races) {
            val textButton = TextButton(race.name, borderlessTextButtonStyle)
            textButton.addListener(
                    UIClickListener(textButton,
                            {
                                selection = race
                                for (option in allOptions) {
                                    option.isChecked = false
                                }
                                textButton.isChecked = true
                            })
            )
            this.add(textButton).row()
            allOptions.add(textButton)
        }
    }
}