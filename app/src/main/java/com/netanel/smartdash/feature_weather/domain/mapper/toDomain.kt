package com.netanel.smartdash.feature_weather.domain.mapper

import com.netanel.smartdash.feature_weather.domain.dto.OpenWeatherResponse
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow


fun OpenWeatherResponse.toDomain(): WeatherNow =
    WeatherNow(
        city = name.orEmpty(),
        // API returns Kelvin by default — convert to °C
        temperatureC = main?.temp?.let { (it - 273.15).toInt() },
        description = weather.firstOrNull()?.description.orEmpty()
    )
