package com.netanel.smartdash.feature_weather.domain.model

data class WeatherNow(
    val city: String,
    val temperatureC: Int?,
    val description: String?
)
