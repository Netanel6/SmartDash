package com.netanel.smartdash.weather

data class WeatherNow(
    val city: String,
    val temperatureC: Int?,
    val description: String?
)
