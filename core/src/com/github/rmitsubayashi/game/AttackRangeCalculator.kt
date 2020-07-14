package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.entity.AttackRange
import com.github.rmitsubayashi.entity.Board
import com.github.rmitsubayashi.entity.Piece

object AttackRangeCalculator {
    fun isInRange(board: Board, attacker: Piece, attacked: Piece): Boolean {
        if (!board.contains(attacker) || !board.contains(attacked)) {
            return false
        }
        if (attacker.attackRange == AttackRange.ranged) {
            return true
        }
        //the rest is melee (can attack top, right, bottom, left squares)
        val adjacentSquares = mutableSetOf<Int>()
        val attackerIndex = board.indexOf(attacker)
        //top
        adjacentSquares.add(attackerIndex-3)
        //bottom
        adjacentSquares.add(attackerIndex+3)
        //left
        if (attackerIndex % 3 != 0) {
            adjacentSquares.add(attackerIndex-1)
        }
        //right
        if (attackerIndex % 3 != 2) {
            adjacentSquares.add(attackerIndex+1)
        }
        val attackedIndex = board.indexOf(attacked)
        return attackedIndex in adjacentSquares
    }

    fun getPossibleAttackTargets(board: Board, attacker: Piece): List<Piece> {
        val possiblePieces = mutableListOf<Piece>()
        for (piece in board) {
            if (piece == null) continue
            if (isInRange(board, attacker, piece)) {
                possiblePieces.add(piece)
            }
        }
        return possiblePieces
    }
}