package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.rmitsubayashi.ui.util.UIClickListener
import com.github.rmitsubayashi.ui.util.appSkin

class UIPiecesToggle(private val uiDeck: UIDeck, private val uiPiecePool: UIPiecePool): Table() {
    private var playerPiecesShown = false
    private var piecePoolShown = false
    val showPiecesButton: Button
    init {
        showPiecesButton = TextButton("My Pieces", appSkin.get("square-selectable", TextButton.TextButtonStyle::class.java))
        val showPoolButton = TextButton("Shop", appSkin.get("square-selectable", TextButton.TextButtonStyle::class.java))
        val buttonTable = Table()
        buttonTable.add(showPiecesButton).height(50f).expand().uniform()
        buttonTable.add(showPoolButton).height(50f).expand().uniform()
        this.add(buttonTable).height(50f).width(480f).row()

        val piecesStack = Stack()
        piecesStack.addActor(uiPiecePool)
        piecesStack.addActor(uiDeck)
        //default should be pool since the player doesn't own any pieces
        piecePoolShown = true
        uiDeck.isVisible = false
        showPoolButton.isChecked = true
        this.add(piecesStack).height(100f).width(480f)

        showPiecesButton.addListener(
                UIClickListener(showPiecesButton, {
                    if (playerPiecesShown) {
                        showPiecesButton.isChecked = true
                        return@UIClickListener
                    }
                    uiPiecePool.isVisible = false
                    uiDeck.isVisible = true
                    playerPiecesShown = true
                    piecePoolShown = false
                    showPoolButton.isChecked = false
                })
        )

        showPoolButton.addListener(
                UIClickListener(showPoolButton, {
                    if (piecePoolShown) {
                        showPoolButton.isChecked = true
                        return@UIClickListener
                    }
                    uiDeck.isVisible = false
                    uiPiecePool.isVisible = true
                    playerPiecesShown = false
                    piecePoolShown = true
                    showPiecesButton.isChecked = false
                })
        )

    }


}