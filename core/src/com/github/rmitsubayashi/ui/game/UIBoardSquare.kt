package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Scaling
import com.github.rmitsubayashi.action.ActionObservable
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.ui.util.UIClickListener

class UIBoardSquare(val squareIndex: Int, player: Player, observable: ActionObservable): Table() {
    var piece: UIPiece? = null
    private val placeHolder: Image = Image()

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
    }

    fun placePiece(piece: UIPiece) {
        this.removeActor(placeHolder)
        this.piece = piece
        piece.setScaling(Scaling.fit)
        this.add(piece)
    }
}