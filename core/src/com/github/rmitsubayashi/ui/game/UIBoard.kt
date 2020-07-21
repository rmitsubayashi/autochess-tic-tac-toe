package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.game.Game

class UIBoard(game: Game): Table() {
    private val squares: List<UIBoardSquare>

    init {
        background = Image(Texture("image/tictactoe.png")).drawable
        val tempList = mutableListOf<UIBoardSquare>()
        for (i in 0..2) {
            for (j in 0..2) {
                val square = UIBoardSquare(i*3+j, game.player1, game.board, game.actionObservable)
                tempList.add(square)
                this.add(square).grow().uniform()
            }
            this.row()
        }
        squares = tempList

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

    fun updatePieceState(piece: Piece) {
        val square = squares.firstOrNull { it.piece?.actualPiece == piece }
        square?.updatePieceState()
    }

    private fun getUIBoardSquare(square: Int) = squares.first { it.squareIndex == square }


}