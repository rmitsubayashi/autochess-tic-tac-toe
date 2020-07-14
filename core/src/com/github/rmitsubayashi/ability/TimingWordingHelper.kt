package com.github.rmitsubayashi.ability

import com.github.rmitsubayashi.action.EventType

object TimingWordingHelper {
    const val beginningOfYourTurn = "At the beginning of your turn, "
    const val endOfYourTurn = "At the end of your turn, "
    const val whenAttacks = "When this piece attacks, "
    const val whenPlayed = "When played, "
    const val whenDestroyed = "When destroyed, "

    fun wordToEventType(word: String): EventType? {
        return when (word) {
            whenPlayed -> EventType.placePiece
            whenAttacks -> EventType.pieceDeclaresAttack
            else -> null
        }
    }
}