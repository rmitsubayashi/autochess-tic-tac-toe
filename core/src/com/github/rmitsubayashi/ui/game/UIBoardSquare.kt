package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.github.rmitsubayashi.action.ActionObservable
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.ui.util.UIClickListener

class UIBoardSquare(val squareIndex: Int, private val player: Player,
                    private val board: Board, observable: ActionObservable): Table() {
    var piece: UIPiece? = null
    private val placeHolder: Image = Image()
    private val pieceState: Table = Table()
    private val secured: Label
    private val isEnemy: Label
    private val hp: Label

    init {
        this.add(placeHolder).grow()
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
        isEnemy = Label("E", labelStyle)
        hp = Label("", labelStyle)
    }

    fun placePiece(piece: UIPiece) {
        this.removeActor(placeHolder)
        this.piece = piece
        piece.setScaling(Scaling.fit)
        updatePieceState()
        this.add(pieceState).align(Align.top)
        this.add(piece)
    }

    fun removePiece(): UIPiece? {
        this.removeActor(piece)
        this.add(placeHolder)
        pieceState.clearChildren()
        this.removeActor(pieceState)
        return piece
    }

    fun updatePieceState() {
        pieceState.clearChildren()
        val p = piece?.actualPiece
        p ?: return
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