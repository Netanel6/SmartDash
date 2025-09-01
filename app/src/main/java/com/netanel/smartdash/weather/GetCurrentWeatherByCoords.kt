package com.netanel.smartdash.weather

import com.netanel.smartdash.domain.ApiResult
import javax.inject.Inject

class GetCurrentWeatherByCoords @Inject constructor(
    private val repo: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double, unit: Char = 'c'): ApiResult<WeatherNow> =
        repo.current(lat, lon, unit)
}
