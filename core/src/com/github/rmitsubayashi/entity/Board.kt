package com.github.rmitsubayashi.entity

class Board: Iterable<Piece?> {
    private val board = arrayOfNulls<Piece?>(9)
    private val secured = BooleanArray(9) { false }
    operator fun get(index: Int) = board[index]
    operator fun set(index: Int, piece: Piece?) {
        board[index] = piece
    }

    override fun iterator(): Iterator<Piece?> = board.iterator()

    fun removePiece(piece: Piece) {
        val index = board.indexOf(piece)
        if (index == -1) return
        board[index] = null
        secured[index] = false
    }

    fun isOnBoard(piece: Piece) = board.contains(piece)

    fun isSecured(piece: Piece?): Boolean {
        val index = board.indexOf(piece)
        if (index == -1) return false
        return secured[index]
    }

    fun secure(piece: Piece) {
        val index = board.indexOf(piece)
        if (index == -1) return
        secured[index] = true
    }
}