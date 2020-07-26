package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.util.UIClickListener

// HUD = Head-Up Display
class UIHUD(private val game: Game, private val player: Player, private val uiChoosePiece: UIChoosePiece): Table() {
    private val moneyLabel: Label
    private val setupFinishedButton: TextButton
    private val scoreLabel: Label
    init {
        val labelStyle = Label.LabelStyle()
        labelStyle.font = BitmapFont()
        scoreLabel = Label(formatScoreText(), labelStyle)
        this.add(scoreLabel).row()
        moneyLabel = Label(player.money.toString(), labelStyle)
        this.add(moneyLabel)
        val buttonStyle = TextButton.TextButtonStyle()
        buttonStyle.font = BitmapFont()
        setupFinishedButton = TextButton("Setup finished", buttonStyle)
        setupFinishedButton.addListener(
                UIClickListener(setupFinishedButton, {
                    game.gameProgressManager.toBattlePhase()
                })
        )
        this.add(setupFinishedButton)
        this.add(uiChoosePiece)
    }

    fun updatePlayerMoney() {
        moneyLabel.setText(player.money.toString())
    }

    fun enableSetupFinishedButton(enabled: Boolean) {
        setupFinishedButton.setLayoutEnabled(enabled)
    }

    fun updateScore() {
        scoreLabel.setText(formatScoreText())
    }

    private fun formatScoreText(): String {
        val otherPlayer = game.getOpposingPlayer(player)
        return "${player.score} - ${otherPlayer?.score}"
    }
}