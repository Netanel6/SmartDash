package com.netanel.smartdash.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow

@Entity(tableName = "weather_cache")
data class WeatherEntity(
    @PrimaryKey val cacheKey: String,
    val latitude: Double,
    val longitude: Double,
    val unit: Char,
    val city: String,
    val temperatureC: Int?,
    val description: String?,
    val updatedAt: Long
)

fun WeatherEntity.toDomain(): WeatherNow = WeatherNow(
    city = city,
    temperatureC = temperatureC,
    description = description
)

fun WeatherNow.toEntity(
    cacheKey: String,
    lat: Double,
    lon: Double,
    unit: Char,
    timestamp: Long
): WeatherEntity = WeatherEntity(
    cacheKey = cacheKey,
    latitude = lat,
    longitude = lon,
    unit = unit,
    city = city,
    temperatureC = temperatureC,
    description = description,
    updatedAt = timestamp
)
