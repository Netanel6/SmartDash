package com.netanel.smartdash.feature_weather.domain.dto
import kotlinx.serialization.Serializable

@Serializable
data class YahooWeatherResponse(
    val location: YahooLocation? = null,
    val current_observation: YahooCurrentObservation? = null,
    val forecasts: List<YahooForecast> = emptyList()
)

@Serializable
data class YahooLocation(
    val city: String? = null,
    val region: String? = null,
    val woeid: String? = null,
    val country: String? = null,
    val lat: String? = null,
    val long: String? = null,
    val timezone_id: String? = null
)

@Serializable
data class YahooCurrentObservation(
    val pubDate: Long? = null,
    val wind: YahooWind? = null,
    val atmosphere: YahooAtmosphere? = null,
    val astronomy: YahooAstronomy? = null,
    val condition: YahooCondition? = null
)

@Serializable data class YahooWind(val chill: Int? = null, val direction: String? = null, val speed: Int? = null)
@Serializable data class YahooAtmosphere(val humidity: Int? = null, val visibility: Double? = null, val pressure: Double? = null)
@Serializable data class YahooAstronomy(val sunrise: String? = null, val sunset: String? = null)
@Serializable data class YahooCondition(val temperature: Int? = null, val text: String? = null, val code: Int? = null)

@Serializable
data class YahooForecast(
    val day: String? = null,
    val date: Long? = null,
    val high: Int? = null,
    val low: Int? = null,
    val text: String? = null,
    val code: Int? = null
)
