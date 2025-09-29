package com.netanel.smartdash.feature_weather.domain.usecase

import com.netanel.smartdash.core.cache.CachePolicy
import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_weather.data.repo.WeatherRepository
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow
import javax.inject.Inject

class GetCurrentWeatherByCoords @Inject constructor(
    private val repo: WeatherRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double,
        unit: Char = 'c',
        policy: CachePolicy = CachePolicy.CACHE_THEN_NETWORK
    ): ApiResult<WeatherNow> =
        repo.current(lat, lon, unit, policy)
}
