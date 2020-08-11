package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.rmitsubayashi.ui.util.UIClickListener
import com.github.rmitsubayashi.ui.util.appSkin

class UIPiecesToggle(private val uiDeck: UIDeck, private val uiShop: UIShop, private val uiHand: UIHand): Table() {
    private var deckShown = false
    private var shopShown = false
    private var handShown = false
    val deckButton: Button
    private val handButton: Button
    private val shopButton: Button
    init {
        deckButton = TextButton("Deck", appSkin.get("square-selectable", TextButton.TextButtonStyle::class.java))
        shopButton = TextButton("Shop", appSkin.get("square-selectable", TextButton.TextButtonStyle::class.java))
        handButton = TextButton("Hand", appSkin.get("square-selectable", TextButton.TextButtonStyle::class.java))
        val buttonTable = Table()
        buttonTable.add(deckButton).height(50f).expand().uniform()
        buttonTable.add(shopButton).height(50f).expand().uniform()
        buttonTable.add(handButton).height(50f).expand().uniform()
        this.add(buttonTable).height(50f).width(480f).row()

        val piecesStack = Stack()
        piecesStack.addActor(uiShop)
        piecesStack.addActor(uiDeck)
        piecesStack.addActor(uiHand)
        //default should be pool since the player doesn't own any pieces
        shopShown = true
        uiDeck.isVisible = false
        uiHand.isVisible = false
        shopButton.isChecked = true
        this.add(piecesStack).height(100f).width(480f)

        deckButton.addListener(
                UIClickListener(deckButton, {
                    if (deckShown) {
                        deckButton.isChecked = true
                        return@UIClickListener
                    }
                    uiShop.isVisible = false
                    uiHand.isVisible = false
                    uiDeck.isVisible = true
                    deckShown = true
                    shopShown = false
                    handShown = false
                    shopButton.isChecked = false
                    handButton.isChecked = false
                })
        )

        shopButton.addListener(
                UIClickListener(shopButton, {
                    clickShop()
                })
        )

        handButton.addListener(
                UIClickListener(handButton, {
                    clickHand()
                })
        )

    }

    fun clickHand() {
        if (handShown) {
            handButton.isChecked = true
            return
        }
        uiDeck.isVisible = false
        uiShop.isVisible = false
        uiHand.isVisible = true
        deckShown = false
        handShown = true
        shopShown = false
        deckButton.isChecked = false
        shopButton.isChecked = false
    }

    fun clickShop() {
        if (shopShown) {
            shopButton.isChecked = true
            return
        }
        uiDeck.isVisible = false
        uiHand.isVisible = false
        uiShop.isVisible = true
        deckShown = false
        handShown = false
        shopShown = true
        deckButton.isChecked = false
        handButton.isChecked = false
    }


}