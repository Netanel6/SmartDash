package com.netanel.smartdash.weather

import com.netanel.smartdash.BuildConfig
import com.netanel.smartdash.domain.ApiResult
import com.netanel.smartdash.domain.HttpClient
import com.netanel.smartdash.domain.HttpMethod
import com.netanel.smartdash.domain.HttpRequest

import javax.inject.Inject

class YahooWeatherRemote @Inject constructor(
    private val http: HttpClient
) {
    suspend fun currentByCoords(
        lat: Double,
        lon: Double,
        unit: Char = 'c'  // 'c' = Celsius, 'f' = Fahrenheit
    ): ApiResult<YahooWeatherResponse> {
        val req = HttpRequest(
            method = HttpMethod.GET,
            url = "https://yahoo-weather5.p.rapidapi.com/weather",
            headers = mapOf(
                "X-RapidAPI-Key" to BuildConfig.RAPID_API_KEY,
                "X-RapidAPI-Host" to BuildConfig.RAPID_API_HOST
            ),
            query = mapOf(
                "lat" to lat,
                "long" to lon,
                "format" to "json",
                "u" to unit
            )
        )
        return http.request(req)
    }
}
