package com.github.rmitsubayashi.ui.game.action

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.AnimationConfig
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.assets.SoundAssets
import com.github.rmitsubayashi.ui.game.UIBoard
import com.github.rmitsubayashi.ui.game.UIPiecePool
import com.github.rmitsubayashi.ui.util.setAlpha

class UpdatePieceState(private val assetManager: AssetManager, private val board: Board,
                       private val uiBoard: UIBoard, private val uiPiecePool: UIPiecePool)
    : Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type !in listOf(EventType.pieceDamaged, EventType.pieceSecured )) return false
        if (event.actor !is Piece) return false
        return true

    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        when (event.type) {
            EventType.pieceDamaged -> {
                val piece = event.actor as Piece
                val uiPiece = uiBoard.findPiece(piece)
                uiPiece ?: return emptyList()
                val originalColor = Color(uiPiece.color)
                val red = Color(0.5f,0f,0f,1f)
                game.animationQueue.addAnimation(
                        AnimationConfig(
                                Actions.color(red, 0.3f),
                                uiPiece,
                                0.3f
                        )
                )
                game.animationQueue.addAnimation(
                        AnimationConfig(
                                Actions.color(originalColor, 0.3f),
                                uiPiece,
                                0.3f
                        )
                )
                if (piece.isDead()) {
                    game.animationQueue.addAnimation(
                            AnimationConfig(
                                    Actions.alpha(0f, 0.3f),
                                    uiPiece,
                                    0.3f
                            ) {
                                uiPiece.setAlpha(1f)
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
                    game.animationQueue.addAnimation(
                            AnimationConfig(
                                    Actions.alpha(0.5f, 0.5f),
                                    securedImage,
                                    0.5f
                            ) {
                                assetManager.get(SoundAssets.secured).play()
                                uiBoard.updatePieceState(piece)
                            }
                    )
                    game.animationQueue.addAnimation(
                            AnimationConfig(
                                    Actions.alpha(0f, 0.5f),
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