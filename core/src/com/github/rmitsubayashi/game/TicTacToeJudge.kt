package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player

object TicTacToeJudge {
    fun listTicTacToeIndexes(board: Board, player: Player): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        //horizontal
        for (i in 0 .. 2) {
            if (
                    isTicTacToeSquare(3*i, player, board) &&
                    isTicTacToeSquare(3*i+1, player, board) &&
                    isTicTacToeSquare(3*i+2, player, board)
            ) {
                result.add(listOf(3*i, 3*i+1, 3*i+2))
            }
        }
        //vertical
        for (j in 0 .. 2) {
            if (
                    isTicTacToeSquare(j, player, board) &&
                    isTicTacToeSquare(j+3, player, board) &&
                    isTicTacToeSquare(j+6, player, board)
            ) {
                result.add(listOf(j, j+3, j+6))
            }
        }
        //diagonal 1
        if (
                isTicTacToeSquare(0, player, board) &&
                isTicTacToeSquare(4, player, board) &&
                isTicTacToeSquare(8, player, board)
        ) {
            result.add(listOf(0, 4, 8))
        }
        //diagonal 2
        if (
                isTicTacToeSquare(2, player, board) &&
                isTicTacToeSquare(4, player, board) &&
                isTicTacToeSquare(6, player, board)
        ) {
            result.add(listOf(2, 4, 6))
        }
        return result
    }

    private fun isTicTacToeSquare(index: Int, player: Player, board: Board): Boolean {
        if (!board.isSecured(index, player)) return false
        return true
    }
}