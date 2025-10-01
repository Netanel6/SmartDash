package com.netanel.smartdash.feature_weather.data.repo

import com.netanel.smartdash.core.cache.CachePolicy
import com.netanel.smartdash.core.database.WeatherDao
import com.netanel.smartdash.core.database.toDomain
import com.netanel.smartdash.core.database.toEntity
import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_weather.data.remote.OpenWeatherRemote
import com.netanel.smartdash.feature_weather.domain.mapper.toDomain
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow
import javax.inject.Inject

interface WeatherRepository {
    suspend fun current(
        lat: Double,
        lon: Double,
        unit: Char = 'c',
        policy: CachePolicy = CachePolicy.CACHE_THEN_NETWORK
    ): ApiResult<WeatherNow>
}

class WeatherRepositoryImpl @Inject constructor(
    private val remote: OpenWeatherRemote,
    private val weatherDao: WeatherDao
) : WeatherRepository {
    override suspend fun current(
        lat: Double,
        lon: Double,
        unit: Char,
        policy: CachePolicy
    ): ApiResult<WeatherNow> {
        val cacheKey = cacheKey(lat, lon, unit)
        val cached = if (policy.shouldReadFromCache) weatherDao.getWeather(cacheKey) else null

        if (!policy.shouldFetchFromNetwork) {
            return cached?.let { ApiResult.Success(it.toDomain(), 200) }
                ?: ApiResult.HttpError(404, "No cached weather data")
        }

        return when (val res = remote.byCoords(lat, lon, headers = policy.toRequestHeaders())) {
            is ApiResult.Success -> {
                val domain = res.value.toDomain()
                weatherDao.upsert(
                    domain.toEntity(
                        cacheKey = cacheKey,
                        lat = lat,
                        lon = lon,
                        unit = unit,
                        timestamp = System.currentTimeMillis()
                    )
                )
                ApiResult.Success(domain, res.code)
            }

            is ApiResult.HttpError -> cached?.let { ApiResult.Success(it.toDomain(), 200) } ?: res
            is ApiResult.NetworkError -> cached?.let { ApiResult.Success(it.toDomain(), 200) } ?: res
            is ApiResult.SerializationError -> cached?.let { ApiResult.Success(it.toDomain(), 200) } ?: res
            is ApiResult.UnknownError -> cached?.let { ApiResult.Success(it.toDomain(), 200) } ?: res
        }
    }

    private fun cacheKey(lat: Double, lon: Double, unit: Char): String =
        "%.4f|%.4f|%c".format(lat, lon, unit.lowercaseChar())
}
