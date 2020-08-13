package com.github.rmitsubayashi.ability

import com.github.rmitsubayashi.action.UnspecifiedTarget

object TargetWordingHelper {
    const val opponentPiece = "an opposing piece"
    const val allOpponentPieces = "all opposing pieces"
    const val allPieces = "all pieces"
    const val randomOpponentPiece = "a random opposing piece"
    const val adjacentPieces = "adjacent pieces"
    const val randomSquare = "a random square"

    fun wordToEventActor(word: String): UnspecifiedTarget? {
        // choosing an opponent
        return when (word) {
            randomOpponentPiece -> UnspecifiedTarget(UnspecifiedTarget.TargetType.RANDOM_OPPONENT)
            allOpponentPieces -> UnspecifiedTarget(UnspecifiedTarget.TargetType.ALL_OPPONENTS)
            opponentPiece -> UnspecifiedTarget(UnspecifiedTarget.TargetType.SELECT_OPPONENT_PIECE)
            adjacentPieces -> UnspecifiedTarget(UnspecifiedTarget.TargetType.ADJACENT_PIECES)
            randomSquare -> UnspecifiedTarget(UnspecifiedTarget.TargetType.RANDOM_SQUARE)
            allPieces -> UnspecifiedTarget(UnspecifiedTarget.TargetType.ALL_PIECES)
            else -> null
        }
    }
}