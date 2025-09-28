package com.netanel.smartdash.feature_coins.domain.mapper

import com.netanel.smartdash.feature_coins.domain.dto.CoinMarketDto
import com.netanel.smartdash.feature_coins.domain.model.TopCoin

fun List<CoinMarketDto>.toDomain(): List<TopCoin> = mapNotNull { dto ->
    val id = dto.id ?: return@mapNotNull null
    val symbol = dto.symbol?.uppercase() ?: "?"
    val name = dto.name ?: symbol
    TopCoin(
        id = id,
        symbol = symbol,
        name = name,
        imageUrl = dto.imageUrl,
        priceUsd = dto.currentPrice,
        marketCapRank = dto.marketCapRank,
        changePercent24h = dto.priceChangePct24h
    )
}
