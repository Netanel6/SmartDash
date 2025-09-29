package com.netanel.smartdash.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val priceUsd: Double?,
    val timestamp: Long
)
