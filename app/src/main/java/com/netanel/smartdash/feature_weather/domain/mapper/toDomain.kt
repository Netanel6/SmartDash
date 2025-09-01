package com.netanel.smartdash.feature_weather.domain.mapper

import com.netanel.smartdash.feature_weather.domain.dto.YahooWeatherResponse
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow


fun YahooWeatherResponse.toDomain(): WeatherNow =
    WeatherNow(
        city = location?.city.orEmpty(),
        temperatureC = current_observation?.condition?.temperature,
        description = current_observation?.condition?.text
    )
