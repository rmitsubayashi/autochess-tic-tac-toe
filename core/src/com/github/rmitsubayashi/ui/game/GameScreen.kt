package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.GdxGame
import com.github.rmitsubayashi.ui.game.action.*
import com.github.rmitsubayashi.ui.util.IStageScreen
import com.github.rmitsubayashi.ui.util.appSkin
import com.github.rmitsubayashi.ui.util.setBackgroundColor

class GameScreen(game: GdxGame): IStageScreen(game) {
    private val uiBoard = UIBoard(game.assetManager, game.game)
    private val uiChoosePiece = UIChoosePiece(game.game)
    private val uiHUD = UIHUD(game.game, game.game.player1, uiChoosePiece)
    private val uiPlayerPieces = UIPlayerPieces(game.assetManager, game.game)
    private val uiPiecePool = UIPiecePool(game.assetManager, game.game, game.game.player1)
    private val uiPiecesToggle = UIPiecesToggle(uiPlayerPieces, uiPiecePool)
    private val uiPieceInfoTooltip = UIPieceInfoTooltip()
    private val uiTurnDisplay = UITurnDisplay(game.game.player1)


    init {
        val table = Table()
        table.setFillParent(true)
        table.setBackgroundColor(appSkin.getColor("white"))
        table.add(uiHUD).width(480f).row()
        table.add(uiBoard).width(400f).height(400f).row()
        table.add(uiPiecesToggle).height(150f).width(480f).row()
        stage.addActor(table)
        stage.addActor(uiPieceInfoTooltip)
        stage.addActor(uiTurnDisplay)

        subscribeAction(UpdatePiecePool(game.game.player1, game.assetManager, uiPiecePool, game.game.piecePool, uiPlayerPieces))
        subscribeAction(UpdateMoney(game.game.player1, uiHUD))
        subscribeAction(ShowPieceInfo(game.game.player1, uiPieceInfoTooltip, game.game.board))
        subscribeAction(HandleBoardClick(game.game.player1, uiPlayerPieces))
        subscribeAction(PlacedPiece(game.game.player1, uiBoard, uiPlayerPieces, uiPiecePool))
        subscribeAction(SoldPiece(game.game.player1, game.assetManager, uiPlayerPieces, uiPiecePool))
        subscribeAction(ToggleSetupPhaseButtons(game.game.player1, uiHUD))
        subscribeAction(UpdatePieceState(game.assetManager, game.game.board, uiBoard, uiPiecePool))
        subscribeAction(AnimateAttack(game.assetManager, uiBoard))
        subscribeAction(ShowTurnDisplay(uiTurnDisplay, uiHUD))
        subscribeAction(ShowTicTacToe(uiHUD, uiBoard))
        subscribeAction(ShowResultScreen(game, game.game.player1))
        subscribeAction(ShowChoosePiece(game.game.player1, uiChoosePiece))

        game.game.animationQueue.setStage(this.stage)
        game.game.gameProgressManager.startGame(game.game.player1)
    }
}