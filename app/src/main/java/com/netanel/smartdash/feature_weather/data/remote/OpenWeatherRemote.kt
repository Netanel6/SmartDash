package com.netanel.smartdash.feature_weather.data.remote

import android.os.Build
import android.util.Log
import androidx.compose.ui.graphics.vector.addPathNodes
import com.netanel.smartdash.BuildConfig
import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.core.network.HttpClient
import com.netanel.smartdash.core.network.HttpMethod
import com.netanel.smartdash.core.network.HttpRequest
import com.netanel.smartdash.feature_weather.domain.dto.OpenWeatherResponse
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

class OpenWeatherRemote @Inject constructor(
    private val http: HttpClient
) {
    /**
     * GET https://open-weather13.p.rapidapi.com/latlon?latitude=..&longitude=..&lang=EN
     * Headers:
     *   X-RapidAPI-Key: <your key>
     *   X-RapidAPI-Host: open-weather13.p.rapidapi.com
     */
    suspend fun byCoords(
        lat: Double,
        lon: Double,
        lang: String = "EN"
    ): ApiResult<OpenWeatherResponse> {
        val base = "https://${BuildConfig.OPEN_WEATHER_HOST}".toHttpUrl()

        val url = base.newBuilder()
            .addPathSegment("latlon")
            .addQueryParameter("latitude", lat.toString())
            .addQueryParameter("longitude", lon.toString())
            .addQueryParameter("lang", lang)
            .build()

            val req = HttpRequest(
                method = HttpMethod.GET,
                url = url.toString(),
                headers = mapOf(
                    "X-RapidAPI-Key" to BuildConfig.OPEN_WEATHER_KEY,
                    "X-RapidAPI-Host" to BuildConfig.OPEN_WEATHER_HOST
                )
            )
            return http.request(req)
        }
    }