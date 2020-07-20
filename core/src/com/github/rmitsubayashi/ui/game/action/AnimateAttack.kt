package com.github.rmitsubayashi.ui.game.action

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.AnimationConfig
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIBoard

class AnimateAttack(private val uiBoard: UIBoard): Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.pieceAttacks) return false
        if (event.actor !is Piece) return false
        if (event.actedUpon !is Piece) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInputResult: List<EventActor>?): List<Event> {
        val attackingUIPiece = uiBoard.findPiece(event.actor as Piece)
        val attackedUIPiece = uiBoard.findPiece(event.actedUpon as Piece)
        if (attackingUIPiece == null || attackedUIPiece == null) return emptyList()
        val attackingPieceCoords = attackingUIPiece.localToStageCoordinates(Vector2(0f,0f))
        val attackedPieceCoords = attackedUIPiece.localToStageCoordinates(Vector2(0f,0f))
        val destX = (attackingPieceCoords.x - attackedPieceCoords.x) / 2
        val destY = (attackingPieceCoords.y - attackedPieceCoords.y) / 2
        game.animationQueue.addAnimation(
                AnimationConfig(
                        Actions.sizeBy(10f, 10f, 0.2f),
                        attackingUIPiece,
                        0.2f
                )
        )
        game.animationQueue.addAnimation(
                AnimationConfig(
                        Actions.moveBy(-destX, -destY, 0.5f, Interpolation.slowFast),
                        attackingUIPiece,
                        0.5f
                )
        )
        game.animationQueue.addAnimation(
                AnimationConfig(
                        Actions.moveBy(destX, destY, 0.5f, Interpolation.fastSlow),
                        attackingUIPiece,
                        0.5f
                )
        )
        game.animationQueue.addAnimation(
                AnimationConfig(
                        Actions.sizeBy(-10f, -10f, 0.2f),
                        attackingUIPiece,
                        0.2f
                )
        )

        return emptyList()
    }

    override fun copy(): Action {
        return AnimateAttack(uiBoard)
    }
}