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
    // phase events
    enterBattlePhase,
    enterSetupPhase,
    // piece events
    pieceDeclaresAttack,
    pieceAttacks,
    pieceDamaged,
    pieceDead,
}