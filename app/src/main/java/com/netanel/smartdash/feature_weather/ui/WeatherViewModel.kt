package com.netanel.smartdash.feature_weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.smartdash.core.geo.LocationProvider
import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow
import com.netanel.smartdash.feature_weather.domain.usecase.GetCurrentWeatherByCoords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    /**
     * Fetch weather once for given coords.
     */
    fun load(lat: Double, lon: Double) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            when (val res = getWeather(lat, lon)) {
                is ApiResult.Success -> _state.value = UiState.Success(res.value)
                is ApiResult.HttpError -> _state.value = UiState.Error("HTTP ${res.code}")
                is ApiResult.NetworkError -> _state.value = UiState.Error("Network error")
                is ApiResult.SerializationError -> _state.value = UiState.Error("Parse error")
                is ApiResult.UnknownError -> _state.value = UiState.Error("Unknown error")
            }
        }
    }

    /**
     * Start continuous location tracking and fetch weather on each update.
     * Call only after permissions are granted.
     */
    fun startLocationTracking() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            locationProvider.pollLocation().collect { loc ->
                loc?.let {
                    when (val res = getWeather(loc.latitude, loc.longitude)) {
                        is ApiResult.Success -> _state.value = UiState.Success(res.value)
                        is ApiResult.HttpError -> _state.value = UiState.Error("HTTP ${res.code}")
                        is ApiResult.NetworkError -> _state.value = UiState.Error("Network error")
                        is ApiResult.SerializationError -> _state.value = UiState.Error("Parse error")
                        is ApiResult.UnknownError -> _state.value = UiState.Error("Unknown error")
                    }
                }
            }
        }
    }
}