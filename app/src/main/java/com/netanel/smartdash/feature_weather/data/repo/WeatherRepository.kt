package com.netanel.smartdash.feature_weather.data.repo

import com.netanel.smartdash.core.cache.CachePolicy
import com.netanel.smartdash.core.database.dao.WeatherDao
import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_weather.data.local.toDomain
import com.netanel.smartdash.feature_weather.data.local.toEntity
import com.netanel.smartdash.feature_weather.data.remote.OpenWeatherRemote
import com.netanel.smartdash.feature_weather.domain.mapper.toDomain
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

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

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override suspend fun current(
        lat: Double,
        lon: Double,
        unit: Char,
        policy: CachePolicy
    ): ApiResult<WeatherNow> {
        val cached = weatherDao.get()?.toDomain()
        return when (policy) {
            CachePolicy.CACHE_ONLY -> {
                if (cached != null) {
                    ApiResult.Success(cached, 200)
                } else {
                    ApiResult.UnknownError(IllegalStateException("No cached weather available"))
                }
            }

            CachePolicy.CACHE_THEN_NETWORK -> {
                if (cached != null) {
                    scope.launch { fetchAndStore(lat, lon, unit) }
                    ApiResult.Success(cached, 200)
                } else {
                    fetchAndStore(lat, lon, unit)
                }
            }

            CachePolicy.NETWORK_ONLY -> fetchAndStore(lat, lon, unit)
        }
    }

    private suspend fun fetchAndStore(
        lat: Double,
        lon: Double,
        unit: Char
    ): ApiResult<WeatherNow> {
        return when (val res = remote.byCoords(lat, lon)) {
            is ApiResult.Success -> {
                val domain = res.value.toDomain()
                val timestamp = System.currentTimeMillis()
                weatherDao.insert(domain.toEntity(timestamp))
                ApiResult.Success(domain, res.code)
            }

            is ApiResult.HttpError -> res
            is ApiResult.NetworkError -> res
            is ApiResult.SerializationError -> res
            is ApiResult.UnknownError -> res
        }
    }
}
