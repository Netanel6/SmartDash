package com.netanel.smartdash.feature_weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.smartdash.core.cache.CachePolicy
import com.netanel.smartdash.core.geo.LocationProvider
import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow
import com.netanel.smartdash.feature_weather.domain.usecase.GetCurrentWeatherByCoords
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.round

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeather: GetCurrentWeatherByCoords,
    private val locationProvider: LocationProvider
) : ViewModel() {

    sealed class UiState {
        data object Idle : UiState()
        data object Loading : UiState()
        data class Success(val data: WeatherNow) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state

    private var lastCoords: Pair<Double, Double>? = null
    private var trackingJob: Job? = null

    /** One-shot fetch for provided coords. */
    fun load(
        lat: Double,
        lon: Double,
        policy: CachePolicy = CachePolicy.CACHE_THEN_NETWORK
    ) {
        lastCoords = lat to lon
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = when (val res = getWeather(lat, lon, policy = policy)) {
                is ApiResult.Success -> UiState.Success(res.value)
                is ApiResult.HttpError -> UiState.Error("HTTP ${res.code}")
                is ApiResult.NetworkError -> UiState.Error("Network error")
                is ApiResult.SerializationError -> UiState.Error("Parse error")
                is ApiResult.UnknownError -> UiState.Error("Unknown error")
            }
        }
    }

    /** Refresh using last known coords (used by the Home/SmartDash screen). */
    fun refresh(policy: CachePolicy = CachePolicy.CACHE_THEN_NETWORK) {
        val (lat, lon) = lastCoords ?: return
        load(lat, lon, policy)
    }

    /**
     * Start polling current location and fetch weather every [pollMs].
     * Defaults to 6 minutes (360_000 ms). Call only after permissions are granted.
     */
    fun startLocationTracking(
        pollMs: Long = 360_000L,
        policy: CachePolicy = CachePolicy.CACHE_THEN_NETWORK
    ) {
        if (trackingJob?.isActive == true) return
        _state.value = UiState.Loading

        trackingJob = viewModelScope.launch {
            locationProvider
                .pollLocation(intervalMs = pollMs)
                // reduce jitter so tiny GPS drift doesn't spam the API
                .map { loc -> loc?.let { roundCoords(it.latitude, it.longitude, 4) } }
                .distinctUntilChanged()
                .collect { coords ->
                    coords ?: return@collect
                    val (lat, lon) = coords
                    lastCoords = lat to lon

                    when (val res = getWeather(lat, lon, policy = policy)) {
                        is ApiResult.Success -> _state.value = UiState.Success(res.value)
                        is ApiResult.HttpError -> _state.value = UiState.Error("HTTP ${res.code}")
                        is ApiResult.NetworkError -> _state.value = UiState.Error("Network error")
                        is ApiResult.SerializationError -> _state.value = UiState.Error("Parse error")
                        is ApiResult.UnknownError -> _state.value = UiState.Error("Unknown error")
                    }
                }
        }
    }

    /** Stop polling (called from screen's onDispose). */
    fun stopLocationTracking() {
        trackingJob?.cancel()
        trackingJob = null
    }

    private fun roundCoords(lat: Double, lon: Double, decimals: Int): Pair<Double, Double> {
        fun r(v: Double): Double {
            val m = 10.0.pow(decimals)
            return round(v * m) / m
        }
        return r(lat) to r(lon)
    }
}
