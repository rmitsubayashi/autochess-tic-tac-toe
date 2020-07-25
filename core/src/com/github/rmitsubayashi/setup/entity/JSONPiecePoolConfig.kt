package com.github.rmitsubayashi.setup.entity

import com.github.rmitsubayashi.game.PiecePool
import com.google.gson.annotations.SerializedName

data class JSONPiecePoolConfig(
        @SerializedName("rerollCost")
        val rerollCost: Int,
        @SerializedName("poolSize")
        val poolSize: Int,
        @SerializedName("distributionPercentageTable")
        val table: Map<String, List<Double>>,
        @SerializedName("amountPerPiece")
        val amounts: Map<String, Int>
) {
        fun toPiecePoolConfig(): PiecePool.Configs {
                return PiecePool.Configs(
                        rerollCost = rerollCost,
                        poolSize = poolSize,
                        distributionPercentageTable = table.map {
                                entry -> PiecePool.Configs.DistributionPercentage(entry.key.toInt(), entry.value)
                        },
                        amountPerPiece = amounts.map {
                                entry -> Pair(entry.key.toInt(), entry.value)
                        }.toMap()
                )
        }
}