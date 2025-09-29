package com.netanel.smartdash.feature_coins.data.local

import com.netanel.smartdash.core.database.entity.CoinEntity
import com.netanel.smartdash.feature_coins.domain.model.TopCoin

fun CoinEntity.toDomain(): TopCoin = TopCoin(
    id = id,
    symbol = symbol,
    name = symbol,
    imageUrl = null,
    priceUsd = priceUsd,
    marketCapRank = null,
    changePercent24h = null
)

fun TopCoin.toEntity(timestamp: Long): CoinEntity = CoinEntity(
    id = id,
    symbol = symbol,
    priceUsd = priceUsd,
    timestamp = timestamp
)
