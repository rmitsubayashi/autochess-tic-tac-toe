package com.github.rmitsubayashi.entity

import kotlin.math.min
import kotlin.random.Random

class Deck {
    val pieces: List<Piece> get() = _pieces
    private val _pieces = mutableListOf<Piece>()

    fun add(piece: Piece) {
        _pieces.add(piece.copy())
    }

    fun draw(cardCt: Int): List<Piece> {
        var cardsLeft = min(cardCt, _pieces.size)
        val cardIndexes = mutableSetOf<Int>()
        // prevent possible infinite loops (might happen with accidental revisions of code)
        var loopCt = 0
        while (cardsLeft > 0) {
            val randomIndex = Random.nextInt(_pieces.size)
            if (!cardIndexes.contains(randomIndex)) {
                cardIndexes.add(randomIndex)
                cardsLeft--
            }
            if (loopCt++ > 1000000) break
        }
        return cardIndexes.map { _pieces[it].copy() }
    }

    fun remove(piece: Piece) {
        // will be a copy
        val toRemove = _pieces.firstOrNull { it.isSamePieceType(piece) }
        if (toRemove != null) {
            _pieces.remove(toRemove)
        }
    }

    fun contains(piece: Piece): Boolean {
        return _pieces.firstOrNull { it.isSamePieceType(piece) } != null
    }
}