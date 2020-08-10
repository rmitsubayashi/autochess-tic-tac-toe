package com.github.rmitsubayashi.game

import com.github.rmitsubayashi.entity.Piece
import com.github.rmitsubayashi.entity.Player
import com.github.rmitsubayashi.entity.Race
import kotlin.math.min
import kotlin.random.Random

class Shop(private val allPieces: List<Piece>, private val configs: Configs, private val player: Player) {
    private val piecesByCost: MutableMap<Int, MutableList<Piece>> = mutableMapOf()
    val lastRolledPieces: List<Piece> get() = _lastRolledPieces
    private val _lastRolledPieces: MutableList<Piece> = mutableListOf()

    fun addRace(race: Race) {
        val racePieces = allPieces.filter { it.race == race }
        for (piece in racePieces) {
            if (piecesByCost[piece.cost] == null) {
                piecesByCost[piece.cost] = mutableListOf()
            }
            val piecesOfSameCost = piecesByCost[piece.cost]!!
            val amountPerPiece = configs.amountPerPiece[piece.cost]
                    ?: throw Exception("Need to set an amount per piece for ${piece.cost} cost pieces")
            for (i in 1 .. amountPerPiece) {
                piecesOfSameCost.add(piece)
            }
        }
    }

    fun refresh() {
        val nextPieces = mutableListOf<Piece>()
        // for cases where the distribution percentage table is like lv 1, ... , lv5~
        // where the player level can exceed the max table level
        val clampedPlayerLevel = min(player.level, configs.distributionPercentageTable.size)
        val distribution = configs.distributionPercentageTable.find { it.level == clampedPlayerLevel }
                ?: throw Exception("could not find distribution table for lvl $clampedPlayerLevel")
        val distributionPicker = createRandomDistributionPicker(distribution)
        for (i in 1 .. configs.poolSize) {
            val cost = generateRandomCost(distributionPicker)
            val possiblePieces = piecesByCost[cost] ?: throw Exception("could not find pieces of cost $cost")
            val randomPiece = possiblePieces.random()
            nextPieces.add(randomPiece.copy())
            // we don't take from the pool until the player buys it
        }

        _lastRolledPieces.clear()
        _lastRolledPieces.addAll(nextPieces)
    }

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
        return distributionPicker[Random.nextInt(100)]
    }

    fun takeFromPool(piece: Piece, player: Player) {
        val originalPiece = findOriginalPieceFromCopy(piece)
        piecesByCost[piece.cost]?.remove(originalPiece)
        _lastRolledPieces.remove(piece)
    }

    fun putBackInPool(piece: Piece) {
        val originalPiece = findOriginalPieceFromCopy(piece)
        piecesByCost[piece.cost]?.add(originalPiece)
    }

    fun getAllPieces(): List<Piece> = allPieces.map { it.copy() }

    fun getPieceNumberOfferedOnReroll() = configs.poolSize

    fun getRerollCost(): Int = configs.rerollCost

    private fun findOriginalPieceFromCopy(copy: Piece): Piece {
        return allPieces.find { it.name == copy.name } ?: throw Exception("No piece with name ${copy.name} found")
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