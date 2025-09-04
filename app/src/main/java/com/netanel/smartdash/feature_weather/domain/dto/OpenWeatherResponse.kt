package com.netanel.smartdash.feature_weather.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpenWeatherResponse(
    val coord: Coord? = null,
    val weather: List<WeatherItem> = emptyList(),
    val base: String? = null,
    val main: Main? = null,
    val visibility: Int? = null,
    val wind: Wind? = null,
    val clouds: Clouds? = null,
    val dt: Long? = null,
    val sys: Sys? = null,
    val timezone: Int? = null,
    val id: Long? = null,
    val name: String? = null,
    val cod: Int? = null
) {
    @Serializable data class Coord(val lon: Double? = null, val lat: Double? = null)

    @Serializable
    data class WeatherItem(
        val id: Int? = null,
        val main: String? = null,
        val description: String? = null,
        val icon: String? = null
    )

    @Serializable
    data class Main(
        val temp: Double? = null,
        val feels_like: Double? = null,
        val temp_min: Double? = null,
        val temp_max: Double? = null,
        val pressure: Int? = null,
        val humidity: Int? = null,
        val sea_level: Int? = null,
        val grnd_level: Int? = null
    )

    @Serializable data class Wind(val speed: Double? = null, val deg: Int? = null)
    @Serializable data class Clouds(val all: Int? = null)

    @Serializable
    data class Sys(
        val type: Int? = null,
        val id: Long? = null,
        val country: String? = null,
        val sunrise: Long? = null,
        val sunset: Long? = null
    )
}
