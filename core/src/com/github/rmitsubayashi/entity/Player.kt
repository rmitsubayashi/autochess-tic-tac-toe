package com.github.rmitsubayashi.entity

import com.github.rmitsubayashi.action.EventActor

class Player(val id: Int): EventActor {
    val deck = Deck()
    val hand = mutableListOf<Piece>()
    var money = 5
    var score = 0
    var level = 1
}