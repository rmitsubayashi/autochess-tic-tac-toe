package com.github.rmitsubayashi.action

data class UnspecifiedTarget(val type: TargetType): EventActor {
    enum class TargetType {
        RANDOM_OPPONENT,
        ALL_OPPONENTS,
        ALL_PIECES,
        ALL_ALLY_PIECES,
        SELECT_ALLY_PIECE,
        SELECT_OPPONENT_PIECE
    }
}