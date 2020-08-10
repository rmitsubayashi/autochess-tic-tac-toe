package com.github.rmitsubayashi.entity

class Board: Iterable<Piece?> {
    private val board = arrayOfNulls<Piece?>(9)
    private val secured = mutableListOf<Player?>()
    operator fun get(index: Int) = board[index]
    operator fun set(index: Int, piece: Piece?) {
        board[index] = piece
    }

    init {
        for (i in 1 .. 9) {
            secured.add(null)
        }
    }

    override fun iterator(): Iterator<Piece?> = board.iterator()

    fun removePiece(piece: Piece) {
        val index = board.indexOf(piece)
        if (index == -1) return
        board[index] = null
    }

    fun isOnBoard(piece: Piece) = board.contains(piece)

    fun isSecured(index: Int, player: Player): Boolean {
        if (index == -1) return false
        return secured[index] == player
    }

    fun getSecuredPlayer(index: Int): Player? = secured[index]

    fun secure(piece: Piece) {
        val index = board.indexOf(piece)
        if (index == -1) return
        secured[index] = piece.player
    }

    fun unsecure(index: Int) {
        if (index !in 0 .. 9) return
        secured[index] =  null
    }
}