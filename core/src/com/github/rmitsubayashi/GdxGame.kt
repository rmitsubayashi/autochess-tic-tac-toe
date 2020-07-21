package com.github.rmitsubayashi

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.github.rmitsubayashi.ability.AbilityList
import com.github.rmitsubayashi.setup.JSONPieceFileReader
import com.github.rmitsubayashi.setup.JSONPiecePoolConfigReader
import com.github.rmitsubayashi.setup.TextPlayerLevelCostReader
import com.github.rmitsubayashi.ui.assets.ImageAssets
import com.github.rmitsubayashi.ui.assets.SoundAssets
import com.github.rmitsubayashi.ui.mainmenu.MainMenuScreen
import com.github.rmitsubayashi.ui.util.IStageScreen
import ktx.async.KtxAsync

class GdxGame : Game() {
    lateinit var game: com.github.rmitsubayashi.game.Game
    val assetManager = AssetManager()

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
        loadAssets()
    }

    // call after game is initialized
    private fun loadAssets() {
        assetManager.load(SoundAssets.attack)
        assetManager.load(SoundAssets.click)
        assetManager.load(SoundAssets.sell)
        assetManager.load(SoundAssets.refresh)
        assetManager.load(SoundAssets.secured)
        assetManager.load(ImageAssets.field)
        assetManager.load(ImageAssets.ticTacToe)
        assetManager.load(ImageAssets.shield)
        val pieces = game.piecePool.getAllPieces()
        for (piece in pieces) {
            assetManager.load(SoundAssets.fromPiece(piece))
            assetManager.load(ImageAssets.fromPiece(piece))
        }
        assetManager.finishLoading()
    }

    override fun dispose() {
        assetManager.dispose()
    }

    fun setScreen(screen: IStageScreen) {
        Gdx.input.inputProcessor = screen.stage
        super.setScreen(screen)
    }
}