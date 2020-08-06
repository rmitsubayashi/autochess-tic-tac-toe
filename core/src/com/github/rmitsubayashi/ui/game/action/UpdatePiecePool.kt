package com.github.rmitsubayashi.ui.game.action

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.action.Action
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.AnimationConfig
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.game.PiecePool
import com.github.rmitsubayashi.ui.assets.SoundAssets
import com.github.rmitsubayashi.ui.game.UIDeck
import com.github.rmitsubayashi.ui.game.UIPiecePool
import com.github.rmitsubayashi.ui.game.UIPiecesToggle

class UpdatePiecePool(
        eventActor: Player,
        private val assetManager: AssetManager,
        private val uiPool: UIPiecePool,
        private val piecePool: PiecePool,
        private val uiDeck: UIDeck,
        private val uiPiecesToggle: UIPiecesToggle
): Action(eventActor) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type !in listOf(EventType.SHOP_REROLL, EventType.buyPiece)) return false
        if (event.data == null || event.data[EventDataKey.DONE] != true) return false
        if (event.type == EventType.buyPiece && event.actedUpon !is Piece) return false
        if (event.actor != eventActor) return false
        return true
    }


    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        when (event.type) {
            EventType.buyPiece -> {
                val piece = event.actedUpon as Piece
                val uiPiece = uiPool.takePiece(piece)
                if (uiPiece != null) {
                    uiDeck.addPiece(uiPiece)
                    assetManager.get(SoundAssets.fromPiece(piece)).play()
                    game.animationQueue.addAnimation(
                            AnimationConfig(
                                    Actions.sequence(
                                            Actions.run {
                                                uiPiecesToggle.deckButton.touchable = Touchable.disabled
                                                uiPiecesToggle.deckButton.isChecked = true
                                            },
                                            Actions.delay(0.4f),
                                            Actions.run {
                                                uiPiecesToggle.deckButton.touchable = Touchable.enabled
                                                uiPiecesToggle.deckButton.isChecked = false
                                            }
                                    ),
                                    uiPiecesToggle.deckButton,
                                    0.4f
                            )
                    )
                }
            }
            EventType.SHOP_REROLL -> {
                val player = event.actor as Player
                val newPieces = piecePool.getPieces(player)
                uiPool.refreshPieces(newPieces)
                // no sound if the reroll isn't user driven
                // (for example rerolls during the setup phase)
                val isUserEvent = event.data?.get(EventDataKey.IS_USER_EVENT)
                if (isUserEvent == null || isUserEvent == true) {
                    assetManager.get(SoundAssets.refresh).play()
                }
            }
            else -> {}
        }

        return listOf(Event(EventType.shouldAnimate, null, null))
    }
    override fun copy(): Action {
        return UpdatePiecePool(eventActor as Player, assetManager, uiPool, piecePool, uiDeck, uiPiecesToggle)
    }
}