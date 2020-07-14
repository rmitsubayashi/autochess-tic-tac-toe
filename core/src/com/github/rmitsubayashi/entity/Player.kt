package com.github.rmitsubayashi.entity

import com.github.rmitsubayashi.action.EventActor

class Player(val id: Int): EventActor {
    val pieces = mutableListOf<Piece>()
    var money = 10
    var score = 0
    var level = 1
}