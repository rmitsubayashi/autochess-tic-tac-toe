package com.github.rmitsubayashi.ui.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.github.rmitsubayashi.game.Game

class UIBoard(game: Game): Table() {
    private val squares: List<UIBoardSquare>

    init {
        background = Image(Texture("tictactoe.png")).drawable
        val tempList = mutableListOf<UIBoardSquare>()
        for (i in 0..2) {
            for (j in 0..2) {
                val square = UIBoardSquare(i*3+j, game.player1, game.actionObservable)
                tempList.add(square)
                this.add(square).grow().uniform()
            }
            this.row()
        }
        squares = tempList

    }

    fun placePiece(uiPiece: UIPiece, square: Int) {
        val uiBoardSquare = getUIBoardSquare(square)
        uiBoardSquare.placePiece(uiPiece)
    }

    private fun getUIBoardSquare(square: Int) = squares.first { it.squareIndex == square }


}