package com.netanel.smartdash.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val city: String,
    val temp: Int?,
    val description: String?,
    val timestamp: Long
)
