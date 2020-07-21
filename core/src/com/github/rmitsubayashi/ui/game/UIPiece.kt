package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Pool
import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.assets.ImageAssets
import com.github.rmitsubayashi.ui.util.ImageUtils
import com.github.rmitsubayashi.ui.util.UILongClickListener

class UIPiece private constructor(val pieceType: Piece, texture: Texture, game: Game):
        Image(texture), Pool.Poolable {
    // the piece type is for when the ui pool is instantiated
    // (references the original pieces in the pool).
    // the actual piece is for when a piece is taken from the pool
    // (references the new piece generated by the pool).
    val actualPiece: Piece? get() = _actualPiece

    private var _actualPiece: Piece? = null

    init {
        // the default tooltip in libgdx doesn't work on android...
        addListener(
                UILongClickListener(
                    {
                        game.actionObservable.notifyAllActions(
                                Event(EventType.pieceLongClicked, actualPiece, null)
                        )
                    }, {
                        game.actionObservable.notifyAllActions(
                            Event(EventType.pieceLongClicked, actualPiece, null, mapOf(Pair(EventDataKey.DONE, true)))
                        )
                    }
                )
        )
    }

    override fun reset() {
        //not sure if anything is needed here
    }

    // the piece we get from the piece pool goes here.
    // need the actual piece instance
    fun setActualPiece(piece: Piece) {
        this._actualPiece = piece
    }

    companion object {
        fun create(assetManager: AssetManager, pieceType: Piece, game: Game): UIPiece {
            val texture = assetManager.get(ImageAssets.fromPiece(pieceType))
            return UIPiece(pieceType, texture, game)
        }
    }
}