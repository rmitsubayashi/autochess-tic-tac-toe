package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.entity.Stats
import com.github.rmitsubayashi.game.Game
import com.github.rmitsubayashi.ui.assets.ImageAssets

class UIBoard(assetManager: AssetManager, game: Game): Table() {
    private val squares: List<UIBoardSquare>
    val boltImage: Image

    init {
        background = Image(assetManager.get(ImageAssets.ticTacToe)).drawable
        boltImage = Image(assetManager.get(ImageAssets.shield))
        boltImage.width = 20f
        boltImage.height = 60f
        boltImage.isVisible = false
        val tempList = mutableListOf<UIBoardSquare>()
        for (i in 0..2) {
            for (j in 0..2) {
                val square = UIBoardSquare(assetManager, i*3+j, game.player1, game.actionObservable, game.board)
                tempList.add(square)
                this.add(square).grow().uniform()
            }
            this.row()
        }
        squares = tempList

    }

    override fun setStage(stage: Stage?) {
        super.setStage(stage)
        stage?.addActor(boltImage)
    }

    fun findPiece(piece: Piece): UIPiece? {
        val square = squares.firstOrNull { it.piece?.actualPiece == piece }
        return square?.piece
    }

    fun placePiece(uiPiece: UIPiece, square: Int) {
        val uiBoardSquare = getUIBoardSquare(square)
        uiBoardSquare.placePiece(uiPiece)
    }

    fun removePiece(piece: Piece): UIPiece? {
        val square = squares.firstOrNull { it.piece?.actualPiece == piece }
        return square?.removePiece()
    }

    fun getSecuredImage(piece: Piece): Image? {
        val square = squares.firstOrNull { it.piece?.actualPiece == piece }
        return square?.secureImage
    }

    fun updatePieceStats(piece: Piece, newStats: Stats) {
        val square = squares.firstOrNull { it.piece?.actualPiece == piece }
        square?.updatePieceStats(newStats)
    }

    fun secureSquare(index: Int, player: Player?) {
        val square = squares[index]
        if (player != null) {
            square.secure(player)
        } else {
            square.unsecure()
        }
    }

    fun getSquareCoords(square: Int): Vector2 {
        val uiSquare = getUIBoardSquare(square)
        val cornerCoords = uiSquare.localToStageCoordinates(Vector2(0f,0f))
        return cornerCoords.apply {
            x += uiSquare.width / 2
            y += uiSquare.height / 2
        }
    }

    private fun getUIBoardSquare(square: Int) = squares.first { it.squareIndex == square }

    fun highlightSquare(squareIndex: Int) {
        squares[squareIndex].highlight()
    }

    fun removeHighlights() {
        for (square in squares) {
            square.removeHighlight()
        }
    }


}