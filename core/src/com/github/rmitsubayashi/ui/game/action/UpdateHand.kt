package com.github.rmitsubayashi.ui.game.action

import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIHand
import com.github.rmitsubayashi.ui.game.UIPiecePool

class UpdateHand(eventActor: Player, private val uiHand: UIHand, private val uiPiecePool: UIPiecePool): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.HAND_CHANGED) return false
        if (event.actor != eventActor) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val player = event.actor as Player
        val newHand = player.hand
        val newUIPieces = newHand.mapNotNull { uiPiecePool.createUIPiece(it) }
        val oldUIPieces = uiHand.setNewPieces(newUIPieces)
        uiPiecePool.returnPieceToPool(oldUIPieces)
        return emptyList()
    }

    override fun copy(): Action {
        return UpdateHand(eventActor as Player, uiHand, uiPiecePool)
    }
}