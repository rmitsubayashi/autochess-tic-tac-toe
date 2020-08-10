package com.github.rmitsubayashi.setup.entity

import com.github.rmitsubayashi.game.Shop
import com.google.gson.annotations.SerializedName

data class JSONShopConfig(
        @SerializedName("rerollCost")
        val rerollCost: Int,
        @SerializedName("poolSize")
        val poolSize: Int,
        @SerializedName("distributionPercentageTable")
        val table: Map<String, List<Double>>,
        @SerializedName("amountPerPiece")
        val amounts: Map<String, Int>
) {
        fun toPiecePoolConfig(): Shop.Configs {
                return Shop.Configs(
                        rerollCost = rerollCost,
                        poolSize = poolSize,
                        distributionPercentageTable = table.map {
                                entry -> Shop.Configs.DistributionPercentage(entry.key.toInt(), entry.value)
                        },
                        amountPerPiece = amounts.map {
                                entry -> Pair(entry.key.toInt(), entry.value)
                        }.toMap()
                )
        }
}