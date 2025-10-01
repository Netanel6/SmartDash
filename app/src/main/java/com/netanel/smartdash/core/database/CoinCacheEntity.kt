package com.netanel.smartdash.core.database

import androidx.room.Entity
import com.netanel.smartdash.feature_coins.domain.model.TopCoin

@Entity(
    tableName = "coin_cache",
    primaryKeys = ["queryKey", "coinId"]
)
data class CoinCacheEntity(
    val queryKey: String,
    val coinId: String,
    val symbol: String,
    val name: String,
    val imageUrl: String?,
    val priceUsd: Double?,
    val marketCapRank: Int?,
    val changePercent24h: Double?,
    val orderIndex: Int,
    val updatedAt: Long
)

fun CoinCacheEntity.toDomain(): TopCoin = TopCoin(
    id = coinId,
    symbol = symbol,
    name = name,
    imageUrl = imageUrl,
    priceUsd = priceUsd,
    marketCapRank = marketCapRank,
    changePercent24h = changePercent24h
)

fun TopCoin.toEntity(
    queryKey: String,
    orderIndex: Int,
    timestamp: Long
): CoinCacheEntity = CoinCacheEntity(
    queryKey = queryKey,
    coinId = id,
    symbol = symbol,
    name = name,
    imageUrl = imageUrl,
    priceUsd = priceUsd,
    marketCapRank = marketCapRank,
    changePercent24h = changePercent24h,
    orderIndex = orderIndex,
    updatedAt = timestamp
)
