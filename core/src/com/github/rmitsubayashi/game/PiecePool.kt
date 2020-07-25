package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import kotlin.math.min
import kotlin.random.Random

class PiecePool(private val pieces: List<Piece>, private val config: Configs, private val random: Random) {
    private val lastRolledPieces = mutableMapOf<Player, MutableList<Piece>>()
    private val piecesByCost: Map<Int, MutableList<Piece>>
    init {
        // set up the pool based on configs and pieces
        val tempMap = mutableMapOf<Int, MutableList<Piece>>()
        val maxCost = pieces.maxBy { it.cost }?.cost ?: throw Exception("No pieces with a cost found")
        for (cost in 1 .. maxCost) {
            val tempList = mutableListOf<Piece>()
            val allPiecesOfCost = pieces.filter { it.cost == cost }
            val amountPerPiece = config.amountPerPiece[cost]
                    ?: throw Exception("Need to set an amount per piece for $cost cost pieces")
            for (i in 1 .. amountPerPiece) {
                tempList.addAll(allPiecesOfCost)
            }
            // now all possible pieces (including duplicates) are in the list
            tempMap[cost] = tempList
        }
        piecesByCost = tempMap
    }

    fun refresh(player: Player) {
        val nextPieces = mutableListOf<Piece>()
        // for cases where the distribution percentage table is like lv 1, ... , lv5~
        // where the player level can exceed the max table level
        val clampedPlayerLevel = min(player.level, config.distributionPercentageTable.size)
        val distribution = config.distributionPercentageTable.find { it.level == clampedPlayerLevel }
                ?: throw Exception("could not find distribution table for lvl $clampedPlayerLevel")
        val distributionPicker = createRandomDistributionPicker(distribution)
        for (i in 1 .. config.poolSize) {
            val cost = generateRandomCost(distributionPicker)
            val possiblePieces = piecesByCost[cost] ?: throw Exception("could not find pieces of cost $cost")
            val randomPiece = possiblePieces.random(random)
            nextPieces.add(randomPiece.copy())
            // we don't take from the pool until the player buys it
        }

        lastRolledPieces[player] = nextPieces
    }

    fun getPieces(player: Player): List<Piece> = lastRolledPieces[player] ?: emptyList()

    private fun createRandomDistributionPicker(table: Configs.DistributionPercentage): IntArray {
        val randomDistributionPicker = IntArray(100)
        var toFillIndex = 0
        for ((cost, percentage) in table.percentageInAscendingCostOrder.withIndex()) {
            val toFillCt = percentage * 100
            while (toFillIndex < toFillCt) {
                randomDistributionPicker[toFillIndex] = cost+1
                toFillIndex++
            }
        }
        assert(toFillIndex == 100)

        return randomDistributionPicker
    }

    private fun generateRandomCost(distributionPicker: IntArray): Int {
        return distributionPicker[random.nextInt(100)]
    }

    fun takeFromPool(piece: Piece, player: Player) {
        val originalPiece = findOriginalPieceFromCopy(piece)
        piecesByCost[piece.cost]?.remove(originalPiece)
        lastRolledPieces[player]?.remove(piece)
    }

    fun putBackInPool(piece: Piece) {
        val originalPiece = findOriginalPieceFromCopy(piece)
        piecesByCost[piece.cost]?.add(originalPiece)
    }

    fun getAllPieces(): List<Piece> = pieces.map { it.copy() }

    fun getPieceNumberOfferedOnReroll() = config.poolSize

    fun getRerollCost(): Int = config.rerollCost

    private fun findOriginalPieceFromCopy(copy: Piece): Piece {
        return pieces.find { it.name == copy.name } ?: throw Exception("No piece with name ${copy.name} found")
    }

    data class Configs (
            val rerollCost: Int,
            val poolSize: Int,
            val distributionPercentageTable: List<DistributionPercentage>,
            val amountPerPiece: Map<Int, Int>
    ) {
        data class DistributionPercentage (
                val level: Int,
                // should add up to 1 (ex. 0.65, 0.20, 0.15)
                val percentageInAscendingCostOrder: List<Double>
        )
    }
}