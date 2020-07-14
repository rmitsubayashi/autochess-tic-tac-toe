package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.util.UIClickListener

class UIPiecePoolSlot(private val player: Player, private val game: Game): Table() {
    var piece: UIPiece? = null
    private var clickListener: UIClickListener? = null

    fun placePiece(piece: UIPiece) {
        this.add(piece)
        this.piece = piece
        // we attach the listener to the piece, not the slot
        // so we can better manage long click vs click listener conflict
        clickListener = UIClickListener(piece, { onClick() })
        piece.addListener(clickListener)

    }

    private fun onClick() {
        piece?.let {
            val event = Event(EventType.buyPiece, player, it.actualPiece)
            game.actionObservable.notifyAllActions(event)
            // there is the possibility that the player could not buy the piece,
            // so do not remove it yet.
            // have the parent class handle whether to remove the piece
        }
    }

    fun removePiece(): UIPiece? {
        if (clickListener != null) {
            piece?.removeListener(clickListener)
        }
        clickListener = null
        this.piece?.remove()
        val tempPiece = this.piece
        this.piece = null
        return tempPiece
    }

}