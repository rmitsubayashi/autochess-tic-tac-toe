package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Touchable
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
import com.github.rmitsubayashi.ui.util.appSkin
import com.github.rmitsubayashi.ui.util.setAlpha
import com.github.rmitsubayashi.ui.util.setBackgroundColor

class UIBoardSquare(assetManager: AssetManager, val squareIndex: Int, private val player: Player,
                    observable: ActionObservable, private val board: Board): Stack() {
    var piece: UIPiece? = null
    private val mainTable = Table()
    private val placeHolder: Image = Image()
    private val pieceState: Table = Table()
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
        secureImage = Image(assetManager.get(ImageAssets.shield))
        secureImage.setAlpha(0f)
        isEnemy = Label("E", appSkin)
        hp = Label("", appSkin)

        this.add(mainTable)
        this.add(secureImage)
        // the image blocks the click listener of the piece otherwise
        secureImage.touchable = Touchable.disabled
    }

    fun placePiece(piece: UIPiece) {
        mainTable.clearChildren()
        this.piece = piece
        piece.setScaling(Scaling.fit)
        updatePieceState()
        mainTable.add(pieceState).pad(4f).align(Align.top)
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
        hp.setText("${p.currHP}/${p.currStats.hp}")
        pieceState.add(hp).row()
        if (p.player != player) {
            pieceState.add(isEnemy).row()
        }
    }

    fun secure(player: Player) {
        val color = if (this.player == player) {
            Color(0.2f, 0.2f, 0.7f, 0.2f)
        } else {
            Color(0.7f, 0.2f, 0.2f, 0.2f)
        }
        mainTable.setBackgroundColor(color)
    }

    fun unsecure() {
        mainTable.background = null
    }

    fun highlight() {
        mainTable.setBackgroundColor(Color(0.2f, 0.2f, 0.2f, 0.2f))
    }

    fun removeHighlight() {
        mainTable.background = null
        setSecuredBackgroundColor()
    }

    private fun setSecuredBackgroundColor() {
        val securedPlayer = board.getSecuredPlayer(squareIndex)
        if (securedPlayer == null) {
            mainTable.background = null
            return
        }
        val color = if (securedPlayer == player) {
            Color(0.2f, 0.2f, 0.7f, 0.2f)
        } else {
            Color(0.7f, 0.2f, 0.2f, 0.2f)
        }
        mainTable.setBackgroundColor(color)
    }
}