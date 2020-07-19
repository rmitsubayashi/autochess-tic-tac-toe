package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.github.rmitsubayashi.ui.util.UIClickListener

class UIPiecesToggle(private val uiPlayerPieces: UIPlayerPieces, private val uiPiecePool: UIPiecePool): Table() {
    private var playerPiecesShown = false
    private var piecePoolShown = false
    init {
        val buttonStyle = TextButton.TextButtonStyle()
        buttonStyle.font = BitmapFont()
        val showPiecesButton = TextButton("My Pieces", buttonStyle)
        val showPoolButton = TextButton("Shop", buttonStyle)
        val buttonTable = Table()
        buttonTable.add(showPiecesButton).height(50f).expand().uniform()
        buttonTable.add(showPoolButton).height(50f).expand().uniform()
        this.add(buttonTable).height(50f).width(this.width).row()

        val piecesStack = Stack()
        piecesStack.addActor(uiPiecePool)
        piecesStack.addActor(uiPlayerPieces)
        //default should be pool since the player doesn't own any pieces
        piecePoolShown = true
        uiPlayerPieces.isVisible = false
        this.add(piecesStack).height(100f).width(Gdx.graphics.width.toFloat())

        showPiecesButton.addListener(
                UIClickListener(showPiecesButton, {
                    if (playerPiecesShown) return@UIClickListener
                    uiPiecePool.isVisible = false
                    uiPlayerPieces.isVisible = true
                    playerPiecesShown = true
                    piecePoolShown = false
                })
        )

        showPoolButton.addListener(
                UIClickListener(showPoolButton, {
                    if (piecePoolShown) return@UIClickListener
                    uiPlayerPieces.isVisible = false
                    uiPiecePool.isVisible = true
                    playerPiecesShown = false
                    piecePoolShown = true
                })
        )

    }


}