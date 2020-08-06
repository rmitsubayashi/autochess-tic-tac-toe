package com.github.rmitsubayashi.ai

import com.github.rmitsubayashi.action.Event
import com.github.rmitsubayashi.action.EventDataKey
import com.github.rmitsubayashi.action.EventType
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.game.AttackRangeCalculator
import com.github.rmitsubayashi.game.Game

class CrudeAISetupPhase: GameAI {
    private lateinit var game: Game
    private lateinit var  player: Player
    override fun execute(game: Game, player: Player) {
        // store them in variables because most private methods use them
        this.game = game
        this.player = player

        // reroll if somehow the player doesn't have pieces
        // (shouldn't happen though)
        if (game.piecePool.getPieces(player).isEmpty()) {
            game.actionObservable.notifyAllActions(
                    Event(EventType.SHOP_REROLL, player, null)
            )
        }

        while (shouldBuyPiecesOrRoll()) {
            // check if a reroll is needed
            val min = calculateMinPiecePoolCost()
            if (playerCannotBuyPieces()) {
                game.actionObservable.notifyAllActions(
                        Event(EventType.SHOP_REROLL, player, null)
                )
            } else {
                var currMin = min
                while (currMin <= player.money) {
                    val possiblePieces = game.piecePool.getPieces(player).filter { it.cost <= player.money }
                    val randomPiece = possiblePieces.random()
                    game.actionObservable.notifyAllActions(
                            Event(EventType.buyPiece, player, randomPiece)
                    )
                    currMin = calculateMinPiecePoolCost()
                    // bought all pieces available
                    if (currMin == -1) {
                        break
                    }
                }
            }
        }

        game.gameProgressManager.toSetupPhase()
        placeAvailablePiecesOnBoard()

    }

    private fun shouldBuyPiecesOrRoll(): Boolean {
        // if the board is full, no reason to buy more pieces at the moment
        val emptyCt = game.board.count { it == null }
        if (emptyCt == 0) return false

        if (playerCannotBuyPieces()) {
            // the player has to have enough money to reroll and buy a 1 cost piece
            // from the next rolled pieces
            val rerollCost = game.piecePool.getRerollCost()
            return player.money > rerollCost
        }
        return true
    }

    private fun calculateMinPiecePoolCost(): Int {
        val costs = game.piecePool.getPieces(player).map { it.cost }
        return costs.min() ?: -1
    }

    // no pieces in current pool or minimum cost too pricey
    private fun playerCannotBuyPieces(): Boolean {
        val minimumPieceCost = calculateMinPiecePoolCost()
        return minimumPieceCost == -1 || minimumPieceCost > player.money
    }

    private fun placeAvailablePiecesOnBoard() {
        // iterate over a copy because the original list will change
        // after placing pieces on the board
        val copy = player.hand.toList()
        for (toPlace in copy) {
            // place pieces adjacent to other ally pieces if possible
            val playerPieces = game.board.filter { it?.player == player }
            val markEmptySquares = game.board.mapIndexed { index, piece ->
                if (piece == null) {
                    index
                } else {
                    -1
                }
            }
            val emptySquares = markEmptySquares.filter { it != -1 }
            if (emptySquares.isEmpty()) {
                return
            }
            val allAdjacentSquares = mutableSetOf<Int>()
            for (piece in playerPieces) {
                val pieceIndex = game.board.indexOf(piece)
                val adjacentSquares = AttackRangeCalculator.getAdjacentSquares(pieceIndex)
                allAdjacentSquares.addAll(adjacentSquares)
            }
            val emptyAdjacentIndexes = allAdjacentSquares.filter { emptySquares.contains(it) }
            val randomIndex = if (emptyAdjacentIndexes.isNotEmpty()) {
                emptyAdjacentIndexes.random()

            } else {
                emptySquares.random()
            }

            game.actionObservable.notifyAllActions(
                    Event(
                            EventType.placePiece, player, toPlace,
                            mapOf(
                                    Pair(EventDataKey.SQUARE, randomIndex),
                                    Pair(EventDataKey.IS_USER_EVENT, false)
                            )
                    )
            )
        }
    }
}