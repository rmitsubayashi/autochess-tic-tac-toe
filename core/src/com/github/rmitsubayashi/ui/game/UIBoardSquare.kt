package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.github.rmitsubayashi.action.ActionObservable
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.ui.assets.ImageAssets
import com.github.rmitsubayashi.ui.util.UIClickListener
import com.github.rmitsubayashi.ui.util.setAlpha

class UIBoardSquare(assetManager: AssetManager, val squareIndex: Int, private val player: Player,
                    private val board: Board, observable: ActionObservable): Stack() {
    var piece: UIPiece? = null
    private val mainTable = Table()
    private val placeHolder: Image = Image()
    private val pieceState: Table = Table()
    private val secured: Label
    val secureImage: Image
    private val isEnemy: Label
    private val hp: Label

    init {
        mainTable.add(placeHolder).grow()
        this.addListener(
            UIClickListener(this,
                {
                    observable.notifyAllActions(
                        Event(
                                EventType.boardClicked, player, null,
                                mapOf(Pair(EventDataKey.SQUARE, squareIndex))
                        )
                    )
                }
            )
        )
        pieceState.pad(5f, 10f, 0f, 0f)
        val labelStyle = Label.LabelStyle()
        labelStyle.font = BitmapFont()
        secured = Label("S", labelStyle)
        secureImage = Image(assetManager.get(ImageAssets.shield))
        secureImage.setAlpha(0f)
        isEnemy = Label("E", labelStyle)
        hp = Label("", labelStyle)

        this.add(mainTable)
        this.add(secureImage)
    }

    fun placePiece(piece: UIPiece) {
        mainTable.clearChildren()
        this.piece = piece
        piece.setScaling(Scaling.fit)
        updatePieceState()
        mainTable.add(pieceState).align(Align.top)
        mainTable.add(piece)
    }

    fun removePiece(): UIPiece? {
        pieceState.clearChildren()
        // if we call removeActor(actor) instead of this, the cells aren't properly cleared
        mainTable.clearChildren()
        mainTable.add(placeHolder).grow()
        return piece
    }

    fun updatePieceState() {
        pieceState.clearChildren()
        val p = piece?.actualPiece
        if (p == null) {
            removePiece()
            return
        }
        hp.setText(p.currHP)
        pieceState.add(hp).row()
        if (board.isSecured(p)) {
            pieceState.add(secured).row()
        }
        if (p.player != player) {
            pieceState.add(isEnemy).row()
        }
    }
}