package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.game.GameProgressManager
import com.github.rmitsubayashi.ui.util.UIClickListener
import com.github.rmitsubayashi.ui.util.appSkin

// HUD = Head-Up Display
class UIHUD(private val game: Game, private val player: Player, uiChoosePiece: UIChoosePiece): Table() {
    private val moneyLabel = Label("", appSkin)
    private val setupFinishedButton: TextButton
    private val scoreLabel = Label("", appSkin.get("title", Label.LabelStyle::class.java))
    private val turnLabel = Label("", appSkin)
    init {
        this.add(turnLabel).width(240f)
        turnLabel.setAlignment(Align.center)
        updateTurn()
        this.add(moneyLabel).width(240f).row()
        updatePlayerMoney()
        moneyLabel.setAlignment(Align.center)
        this.add(scoreLabel).colspan(2).row()
        updateScore()
        setupFinishedButton = TextButton("Setup finished", appSkin.get("borderless", TextButton.TextButtonStyle::class.java))
        setupFinishedButton.addListener(
                UIClickListener(setupFinishedButton, {
                    if (game.gameProgressManager.phase == GameProgressManager.Phase.DECK_BUILDING) {
                        game.gameProgressManager.toSetupPhase()
                    } else if (game.gameProgressManager.phase == GameProgressManager.Phase.SETUP) {
                        game.gameProgressManager.toBattlePhase()
                    }
                })
        )
        this.add(setupFinishedButton).colspan(2).row()
        this.add(uiChoosePiece)
    }

    fun updatePlayerMoney() {
        moneyLabel.setText("${player.money.toString()}g")
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

    fun updateTurn() {
        val playerInt = if (game.gameProgressManager.currPlayer == game.player1) 1 else 2
        turnLabel.setText("Player $playerInt's turn")
    }

    fun setSetupFinishedButtonText(text: String) {
        setupFinishedButton.setText(text)
    }
}