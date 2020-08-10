package com.github.rmitsubayashi.action

enum class EventType {
    // player events
    reroll,
    SHOP_REROLL,
    HAND_CHANGED,
    levelUp,
    buyPiece,
    sellPiece,
    placePiece,
    playerWins,
    moneyChanged,
    pieceLongClicked,
    boardClicked,
    scored,
    userInputRequested,
    CHOOSE_RACE,
    // phase events. events should be indirectly fired from the manager
    enterBattlePhase,
    ENTER_SETUP_PHASE,
    ENTER_SECURE_PHASE,
    ENTER_MONEY_DISTRIBUTION_PHASE,
    ENTER_DECK_BUILDING_PHASE,
    // piece events
    pieceDeclaresAttack,
    pieceAttacks,
    pieceDamaged,
    SQUARE_SECURED,
    // other
    shouldAnimate,
}