package com.github.rmitsubayashi.action

enum class EventType {
    // player events
    reroll,
    levelUp,
    buyPiece,
    sellPiece,
    placePiece,
    playerWins,
    moneyChanged,
    pieceLongClicked,
    boardClicked,
    // phase events. events should be indirectly fired from the manager
    enterBattlePhase,
    enterSetupPhase,
    // piece events
    pieceDeclaresAttack,
    pieceAttacks,
    pieceDamaged,
}