package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.entity.Player

object TicTacToeJudge {
    fun listTicTacToeIndexes(board: Board, player: Player): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        //horizontal
        for (i in 0 .. 2) {
            val h1 = board[3*i]
            val h2 = board[3*i+1]
            val h3 = board[3*i+2]
            if (!board.isSecured(h1) || !board.isSecured(h2) || !board.isSecured(h3)) {
                continue
            }
            if (
                    h1?.player?.id == player.id &&
                    h2?.player?.id == player.id &&
                    h3?.player?.id == player.id
            ) {
                result.add(listOf(3*i, 3*i+1, 3*i+2))
            }
        }
        //vertical
        for (j in 0 .. 2) {
            val v1 = board[j]
            val v2 = board[j+3]
            val v3 = board[j+6]
            if (!board.isSecured(v1) || !board.isSecured(v2) || !board.isSecured(v3)) {
                continue
            }
            if (
                    v1?.player?.id == player.id &&
                    v2?.player?.id == player.id &&
                    v3?.player?.id == player.id
            ) {
                result.add(listOf(j, j+3, j+6))
            }
        }
        //diagonal 1
        val d1 = board[0]
        val d2 = board[4]
        val d3 = board[8]
        if (
                board.isSecured(d1) &&
                board.isSecured(d2) &&
                board.isSecured(d3) &&
                d1?.player?.id == player.id &&
                d2?.player?.id == player.id &&
                d3?.player?.id == player.id
        ) {
            result.add(listOf(0, 4, 8))
        }
        //diagonal 2
        val d4 = board[2]
        val d5 = board[4]
        val d6 = board[6]
        if (
                board.isSecured(d4) &&
                board.isSecured(d5) &&
                board.isSecured(d6) &&
                d4?.player?.id == player.id &&
                d5?.player?.id == player.id &&
                d6?.player?.id == player.id
        ) {
            result.add(listOf(2, 4, 6))
        }
        return result
    }
}