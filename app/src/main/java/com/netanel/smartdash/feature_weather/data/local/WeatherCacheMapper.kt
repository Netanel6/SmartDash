package com.netanel.smartdash.feature_weather.data.local

import com.netanel.smartdash.core.database.entity.WeatherEntity
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow

fun WeatherEntity.toDomain(): WeatherNow = WeatherNow(
    city = city,
    temperatureC = temp,
    description = description
)

fun WeatherNow.toEntity(timestamp: Long): WeatherEntity = WeatherEntity(
    city = city.ifEmpty { "Unknown" },
    temp = temperatureC,
    description = description,
    timestamp = timestamp
)
