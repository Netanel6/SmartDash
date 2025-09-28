package com.netanel.smartdash.feature_coins.domain.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinMarketDto(
    val id: String? = null,
    val symbol: String? = null,
    val name: String? = null,
    @SerialName("image") val imageUrl: String? = null,
    @SerialName("current_price") val currentPrice: Double? = null,
    @SerialName("market_cap_rank") val marketCapRank: Int? = null,
    @SerialName("price_change_percentage_24h") val priceChangePct24h: Double? = null
)
