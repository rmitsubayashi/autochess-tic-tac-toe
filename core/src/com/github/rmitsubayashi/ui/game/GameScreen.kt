package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.GdxGame
import com.github.rmitsubayashi.ui.game.action.*
import com.github.rmitsubayashi.ui.util.IStageScreen

class GameScreen(game: GdxGame): IStageScreen(game) {
    private val uiBoard = UIBoard(game.game)
    private val uiHUD = UIHUD(game.game.player1)
    private val uiPlayerPieces = UIPlayerPieces()
    private val uiPiecePool = UIPiecePool(game.game, game.game.player1)
    private val uiPieceInfoTooltip = UIPieceInfoTooltip()

    init {
        val table = Table()
        table.setFillParent(true)
        table.add(uiHUD).row()
        table.add(uiBoard).width(400f).height(400f).row()
        table.add(uiPlayerPieces).height(100f).row()
        table.add(uiPiecePool).row()
        stage.addActor(table)
        stage.addActor(uiPieceInfoTooltip)

        subscribeAction(UpdatePiecePool(game.game.player1, uiPiecePool, game.game.piecePool, uiPlayerPieces))
        subscribeAction(UpdateMoney(game.game.player1, uiHUD))
        subscribeAction(ShowPieceInfo(game.game.player1, uiPieceInfoTooltip))
        subscribeAction(HandleBoardClick(game.game.player1, uiPlayerPieces))
        subscribeAction(PlacedPiece(game.game.player1, uiBoard, uiPlayerPieces))
    }
}