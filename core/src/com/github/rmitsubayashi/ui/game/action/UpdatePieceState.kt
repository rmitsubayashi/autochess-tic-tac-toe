package com.github.rmitsubayashi.ui.game.action

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.action.EmptyEventActor
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.AnimationConfig
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.assets.SoundAssets
import com.github.rmitsubayashi.ui.game.UIBoard
import com.github.rmitsubayashi.ui.game.UIPiecePool

class UpdatePieceState(private val assetManager: AssetManager, private val board: Board,
                       private val uiBoard: UIBoard, private val uiPiecePool: UIPiecePool)
    : Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type !in listOf(EventType.pieceDamaged, EventType.pieceSecured )) return false
        if (event.actor !is Piece) return false
        return true

    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        when (event.type) {
            EventType.pieceDamaged -> {
                val piece = event.actor as Piece
                if (piece.isDead()) {
                    val uiPiece = uiBoard.findPiece(piece)
                    uiPiece ?: return emptyList()
                    game.animationQueue.addAnimation(
                            AnimationConfig(
                                    Actions.alpha(0f, 0.3f),
                                    uiPiece,
                                    0.3f
                            ) {
                                val toRemoveUIPiece = uiBoard.removePiece(piece)
                                if (toRemoveUIPiece != null) {
                                    uiPiecePool.returnPieceToPool(toRemoveUIPiece)
                                }
                            }
                    )
                } else {
                    uiBoard.updatePieceState(piece)
                }
            }
            EventType.pieceSecured -> {
                val piece = event.actor as Piece
                val securedImage = uiBoard.getSecuredImage(piece)
                if (securedImage != null) {
                    uiBoard.updatePieceState(piece)
                    game.animationQueue.addAnimation(
                            AnimationConfig(
                                    Actions.alpha(0.5f),
                                    securedImage,
                                    0.5f
                            ) {
                                assetManager.get(SoundAssets.secured).play()
                            }
                    )
                    game.animationQueue.addAnimation(
                            AnimationConfig(
                                    Actions.alpha(0f),
                                    securedImage,
                                    0.5f
                            )
                    )
                }
            }
            else -> {}
        }
        return emptyList()
    }

    override fun copy(): Action {
        return UpdatePieceState(assetManager, board, uiBoard, uiPiecePool)
    }
}