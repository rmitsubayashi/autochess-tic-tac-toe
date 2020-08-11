package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.GdxGame
import com.github.rmitsubayashi.ui.game.action.*
import com.github.rmitsubayashi.ui.util.IStageScreen
import com.github.rmitsubayashi.ui.util.appSkin
import com.github.rmitsubayashi.ui.util.setBackgroundColor

class GameScreen(game: GdxGame): IStageScreen(game) {
    private val uiPiecePool = UIPiecePool(game.assetManager, game.game)
    private val uiBoard = UIBoard(game.assetManager, game.game)
    private val uiChoosePiece = UIChoosePiece(game.game)
    private val uiHUD = UIHUD(game.game, game.game.player1, uiChoosePiece)
    private val uiDeck = UIDeck(game.assetManager, game.game)
    private val uiHand = UIHand(4)
    private val uiShop = UIShop(game.game, game.game.player1, uiPiecePool)
    private val uiPiecesToggle = UIPiecesToggle(uiDeck, uiShop, uiHand)
    private val uiPieceInfoTooltip = UIPieceInfoTooltip()
    private val uiTurnDisplay = UIPhaseDisplay(game.game.player1)


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

        subscribeAction(UpdateShop(game.game.player1, game.assetManager, uiShop, game.game.getShop(game.game.player1)!!,
                uiDeck, uiPiecesToggle))
        subscribeAction(UpdateMoney(game.game.player1, uiHUD))
        subscribeAction(ShowPieceInfo(uiPieceInfoTooltip, game.game.board))
        subscribeAction(HandleBoardClick(game.game.player1, uiHand))
        subscribeAction(PlacedPiece(uiBoard, uiHand, uiPiecePool))
        subscribeAction(SoldPiece(game.game.player1, game.assetManager, uiDeck, uiPiecePool))
        subscribeAction(ToggleSetupPhaseButtons(game.game.player1, uiHUD, uiPiecesToggle))
        subscribeAction(UpdatePieceState(game.assetManager, game.game.board, uiBoard, uiPiecePool))
        subscribeAction(AnimateAttack(game.assetManager, uiBoard))
        subscribeAction(ShowPhaseDisplay(uiTurnDisplay, uiHUD))
        subscribeAction(ShowTicTacToe(uiHUD, uiBoard))
        subscribeAction(ShowResultScreen(game, game.game.player1))
        subscribeAction(ShowChoosePiece(game.game.player1, uiChoosePiece, uiBoard))
        subscribeAction(UpdateHand(game.game.player1, uiHand, uiPiecePool))

        game.game.animationQueue.setStage(this.stage)
        game.game.gameProgressManager.startGame(game.game.player1)
    }
}