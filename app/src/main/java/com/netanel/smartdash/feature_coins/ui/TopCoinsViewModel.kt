package com.netanel.smartdash.feature_coins.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_coins.domain.model.TopCoin
import com.netanel.smartdash.feature_coins.domain.usecase.GetTopCoins
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TopCoinsViewModel @Inject constructor(
    private val getTopCoins: GetTopCoins
) : ViewModel() {

    sealed class UiState {
        data object Idle : UiState()
        data object Loading : UiState()
        data class Success(val coins: List<TopCoin>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state

    init {
        refresh()
    }

    fun refresh(limit: Int = 5) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = when (val res = getTopCoins(limit = limit)) {
                is ApiResult.Success -> UiState.Success(res.value)
                is ApiResult.HttpError -> UiState.Error("HTTP ${res.code}")
                is ApiResult.NetworkError -> UiState.Error("Network error")
                is ApiResult.SerializationError -> UiState.Error("Parse error")
                is ApiResult.UnknownError -> UiState.Error("Unknown error")
            }
        }
    }
}
