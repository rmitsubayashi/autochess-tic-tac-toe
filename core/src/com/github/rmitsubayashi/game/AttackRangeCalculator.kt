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
        //the rest is melee
        val attackerIndex = board.indexOf(attacker)
        val attackedIndex = board.indexOf(attacked)
        val adjacentSquares = getAdjacentSquares(attackerIndex)
        return attackedIndex in adjacentSquares
    }

    fun getPossibleAttackTargets(board: Board, attacker: Piece): List<Piece> {
        val possiblePieces = mutableListOf<Piece>()
        for (piece in board) {
            if (piece == null) continue
            if (attacker.player == piece.player) continue
            if (isInRange(board, attacker, piece)) {
                possiblePieces.add(piece)
            }
        }
        return possiblePieces
    }

    fun getAdjacentSquares(square: Int): List<Int> {
        val adjacentSquares = mutableSetOf<Int>()
        if (hasTop(square)) {
            adjacentSquares.add(square - 3)
        }
        if (hasBottom(square)) {
            adjacentSquares.add(square + 3)
        }
        if (hasLeft(square)) {
            adjacentSquares.add(square-1)
        }
        if (hasRight(square)) {
            adjacentSquares.add(square+1)
        }
        if (hasTop(square) && hasRight(square)) {
            adjacentSquares.add(square - 2)
        }
        if (hasTop(square) && hasLeft(square)) {
            adjacentSquares.add(square - 4)
        }
        if (hasBottom(square) && hasRight(square)) {
            adjacentSquares.add(square + 4)
        }
        if (hasBottom(square) && hasLeft(square)) {
            adjacentSquares.add(square + 2)
        }
        return adjacentSquares.toList()
    }

    private fun hasTop(index: Int) = index > 2
    private fun hasBottom(index: Int) = index < 6
    private fun hasLeft(index: Int) = index % 3 != 0
    private fun hasRight(index: Int) = index % 3 != 2
}