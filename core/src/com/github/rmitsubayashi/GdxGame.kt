package com.github.rmitsubayashi

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.github.rmitsubayashi.setup.JSONPieceFileReader
import com.github.rmitsubayashi.setup.JSONPiecePoolConfigReader
import com.github.rmitsubayashi.setup.TextPlayerLevelCostReader
import com.github.rmitsubayashi.ui.mainmenu.MainMenuScreen
import com.github.rmitsubayashi.ui.util.IStageScreen
import ktx.async.KtxAsync

class GdxGame : Game() {
    lateinit var game: com.github.rmitsubayashi.game.Game

    override fun create() {
        KtxAsync.initiate()
        setupGame()

        setScreen(MainMenuScreen(this))
    }

    private fun setupGame() {
        // can't read files in the constructor because the file system is still unavailable
        val pieceReader = JSONPieceFileReader("pieces.json")
        val piecePoolConfigReader = JSONPiecePoolConfigReader("piecePoolConfigs.json")
        val playerLevelCostReader = TextPlayerLevelCostReader("playerLevelCost.txt")
        game = com.github.rmitsubayashi.game.Game(pieceReader, piecePoolConfigReader, playerLevelCostReader)

    }

    override fun dispose() {
    }

    fun setScreen(screen: IStageScreen) {
        Gdx.input.inputProcessor = screen.stage
        super.setScreen(screen)
    }
}