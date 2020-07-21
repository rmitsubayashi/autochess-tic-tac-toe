package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.GdxGame
import com.github.rmitsubayashi.ui.assets.ImageAssets
import com.github.rmitsubayashi.ui.assets.SoundAssets
import com.github.rmitsubayashi.ui.game.action.*
import com.github.rmitsubayashi.ui.util.IStageScreen

class GameScreen(game: GdxGame): IStageScreen(game) {
    private val uiBoard = UIBoard(game.assetManager, game.game)
    private val uiHUD = UIHUD(game.game, game.game.player1)
    private val uiPlayerPieces = UIPlayerPieces(game.game)
    private val uiPiecePool = UIPiecePool(game.assetManager, game.game, game.game.player1)
    private val uiPiecesToggle = UIPiecesToggle(uiPlayerPieces, uiPiecePool)
    private val uiPieceInfoTooltip = UIPieceInfoTooltip()

    init {
        val table = Table()
        table.setFillParent(true)
        table.background = Image(game.assetManager.get(ImageAssets.field)).drawable
        table.add(uiHUD).row()
        table.add(uiBoard).width(400f).height(400f).row()
        table.add(uiPiecesToggle).height(150f).width(Gdx.graphics.width.toFloat()).row()
        stage.addActor(table)
        stage.addActor(uiPieceInfoTooltip)

        subscribeAction(UpdatePiecePool(game.game.player1, uiPiecePool, game.game.piecePool, uiPlayerPieces))
        subscribeAction(UpdateMoney(game.game.player1, uiHUD))
        subscribeAction(ShowPieceInfo(game.game.player1, uiPieceInfoTooltip))
        subscribeAction(HandleBoardClick(game.game.player1, uiPlayerPieces))
        subscribeAction(PlacedPiece(game.game.player1, uiBoard, uiPlayerPieces, uiPiecePool))
        subscribeAction(SoldPiece(game.game.player1, uiPlayerPieces, uiPiecePool))
        subscribeAction(ToggleSetupPhaseButtons(game.game.player1, uiHUD))
        subscribeAction(UpdatePieceState(game.game.board, uiBoard, uiPiecePool))
        subscribeAction(AnimateAttack(uiBoard))

        game.game.gameProgressManager.nextPlayerTurn()
    }
}