package com.netanel.smartdash.feature_weather.data.repo

import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_weather.data.remote.OpenWeatherRemote

import com.netanel.smartdash.feature_weather.domain.model.WeatherNow
import com.netanel.smartdash.feature_weather.domain.mapper.toDomain
import javax.inject.Inject

interface WeatherRepository {
    suspend fun current(lat: Double, lon: Double, unit: Char = 'c'): ApiResult<WeatherNow>
}

class WeatherRepositoryImpl @Inject constructor(
    private val remote: OpenWeatherRemote
) : WeatherRepository {
    override suspend fun current(lat: Double, lon: Double, unit: Char): ApiResult<WeatherNow> {
        return when (val res = remote.byCoords(lat, lon)) {
            is ApiResult.Success -> ApiResult.Success(res.value.toDomain(), res.code)
            is ApiResult.HttpError -> res
            is ApiResult.NetworkError -> res
            is ApiResult.SerializationError -> res
            is ApiResult.UnknownError -> res
        }
    }
}
