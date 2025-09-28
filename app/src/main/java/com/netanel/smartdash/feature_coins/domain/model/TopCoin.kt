package com.netanel.smartdash.feature_coins.domain.model

data class TopCoin(
    val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String?,
    val priceUsd: Double?,
    val marketCapRank: Int?,
    val changePercent24h: Double?
)
