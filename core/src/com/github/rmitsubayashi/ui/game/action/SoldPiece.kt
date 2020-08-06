package com.github.rmitsubayashi.ui.game.action

import com.badlogic.gdx.assets.AssetManager
import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.assets.SoundAssets
import com.github.rmitsubayashi.ui.game.UIPiecePool
import com.github.rmitsubayashi.ui.game.UIDeck

class SoldPiece(eventActor: EventActor, private val assetManager: AssetManager, private val uiDeck: UIDeck, private val uiPiecePool: UIPiecePool)
    : Action(eventActor){
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.sellPiece) return false
        if (event.actor !is Player) return false
        if (event.actor != eventActor) return false
        if (event.actedUpon !is Piece) return false
        if (event.data?.get(EventDataKey.DONE) != true) return false

        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        val piece = event.actedUpon as Piece
        val uiPiece = uiDeck.removePiece(piece)
        if (uiPiece != null) {
            uiPiecePool.returnPieceToPool(uiPiece)
            assetManager.get(SoundAssets.sell).play()
        }
        return emptyList()
    }

    override fun copy(): Action {
        return SoldPiece(eventActor, assetManager, uiDeck, uiPiecePool)
    }
}