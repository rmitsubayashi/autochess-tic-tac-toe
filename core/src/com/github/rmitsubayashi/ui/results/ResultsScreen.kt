package com.github.rmitsubayashi.ui.results

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.rmitsubayashi.GdxGame
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.GameState
import com.github.rmitsubayashi.ui.util.IStageScreen
import com.github.rmitsubayashi.ui.util.UIClickListener

class ResultsScreen(game: GdxGame, player: Player): IStageScreen(game) {
    private val playAgainButton: Button
    private val winnerLabel: Label

    init {
        val table = Table()
        val labelStyle = Label.LabelStyle()
        labelStyle.font = BitmapFont()
        // assuming that the game is not in progress
        val win = game.game.gameJudge.checkWinner() == GameState.Winner(player)
        val resultText = if (win) { "You win!" } else { "You lose!" }
        winnerLabel = Label(resultText, labelStyle)
        table.add(winnerLabel)

        val buttonStyle = TextButton.TextButtonStyle()
        buttonStyle.font = BitmapFont()
        playAgainButton = TextButton("Play Again", buttonStyle)
        playAgainButton.addListener(
                UIClickListener(
                        playAgainButton,
                        { game.restart() }
                )
        )
        table.add(playAgainButton)
        table.setFillParent(true)

        stage.addActor(table)
    }
}