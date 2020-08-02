package com.github.rmitsubayashi.ui.game.action

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.github.rmitsubayashi.action.*
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.AnimationConfig
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.game.UIBoard
import com.github.rmitsubayashi.ui.game.UIHUD

class ShowTicTacToe(private val uihud: UIHUD, private val uiBoard: UIBoard)
    : Action(EmptyEventActor()) {
    override fun conditionMet(game: Game, event: Event): Boolean {
        if (event.type != EventType.scored) return false
        if (event.data?.get(EventDataKey.SQUARE) == null) return false
        return true
    }

    override fun execute(game: Game, event: Event, userInput: Piece?): List<Event> {
        uihud.updateScore()
        @Suppress("UNCHECKED_CAST")
        val squares = event.data?.get(EventDataKey.SQUARE) as List<Int>
        val firstIndex = squares[0]
        val lastIndex = squares[2]
        val startCoords = uiBoard.getSquareCoords(firstIndex)
        val endCoords = uiBoard.getSquareCoords(lastIndex)
        val angle = if (lastIndex - firstIndex == 2) {
            90f
        } else if (lastIndex - firstIndex == 6) {
            180f
        } else if (lastIndex == 8) {
            135f
        } else {
            45f
        }
        game.animationQueue.addAnimation(
                AnimationConfig(
                        Actions.sequence(
                                Actions.show(),
                                Actions.rotateTo(angle),
                                Actions.moveTo(startCoords.x, startCoords.y),
                                Actions.moveTo(endCoords.x, endCoords.y, 0.5f),
                                Actions.hide()
                        ),
                        uiBoard.boltImage,
                        0.5f
                )
        )
        return emptyList()
    }

    override fun copy(): Action {
        return ShowTicTacToe(uihud, uiBoard)
    }
}