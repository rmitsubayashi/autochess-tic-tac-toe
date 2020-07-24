package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player

object TicTacToeJudge {
    fun listTicTacToeIndexes(board: Board, player: Player): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        //horizontal
        for (i in 0 .. 2) {
            val h1 = board[3*i]
            val h2 = board[3*i+1]
            val h3 = board[3*i+2]
            if (
                    isTicTacToeSquare(h1, player, board) &&
                    isTicTacToeSquare(h2, player, board) &&
                    isTicTacToeSquare(h3, player, board)
            ) {
                result.add(listOf(3*i, 3*i+1, 3*i+2))
            }
        }
        //vertical
        for (j in 0 .. 2) {
            val v1 = board[j]
            val v2 = board[j+3]
            val v3 = board[j+6]
            if (
                    isTicTacToeSquare(v1, player, board) &&
                    isTicTacToeSquare(v2, player, board) &&
                    isTicTacToeSquare(v3, player, board)
            ) {
                result.add(listOf(j, j+3, j+6))
            }
        }
        //diagonal 1
        val d1 = board[0]
        val d2 = board[4]
        val d3 = board[8]
        if (
                isTicTacToeSquare(d1, player, board) &&
                isTicTacToeSquare(d2, player, board) &&
                isTicTacToeSquare(d3, player, board)
        ) {
            result.add(listOf(0, 4, 8))
        }
        //diagonal 2
        val d4 = board[2]
        val d5 = board[4]
        val d6 = board[6]
        if (
                isTicTacToeSquare(d4, player, board) &&
                isTicTacToeSquare(d5, player, board) &&
                isTicTacToeSquare(d6, player, board)
        ) {
            result.add(listOf(2, 4, 6))
        }
        return result
    }

    private fun isTicTacToeSquare(piece: Piece?, player: Player, board: Board): Boolean {
        if (piece == null) return false
        if (piece.isDead()) return false
        if (!board.isSecured(piece)) return false
        if (piece.player?.id != player.id) return false
        return true
    }
}