package com.netanel.smartdash.app

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.netanel.smartdash.app.ui.ErrorCard
import com.netanel.smartdash.app.ui.LoadingCard
import com.netanel.smartdash.feature_coins.domain.model.TopCoin
import com.netanel.smartdash.feature_coins.ui.TopCoinsCard
import com.netanel.smartdash.feature_coins.ui.TopCoinsViewModel
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow
import com.netanel.smartdash.feature_weather.ui.WeatherCard
import com.netanel.smartdash.feature_weather.ui.WeatherViewModel
import kotlin.collections.buildList

/* ---------- Dashboard cell models ---------- */
sealed interface DashCell { val key: String }

/** Weather cell (first cell type) */
data class WeatherCell(
    val data: WeatherNow,
    val onClick: () -> Unit
) : DashCell {
    override val key: String = "cell_weather"
}

/** Top crypto coins cell */
data class TopCoinsCell(
    val coins: List<TopCoin>,
    val onClick: () -> Unit
) : DashCell {
    override val key: String = "cell_top_coins"
}

/* ---------- Screen ---------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartDashScreen(
    p: PaddingValues,
    weatherVm: WeatherViewModel = hiltViewModel(),
    coinsVm: TopCoinsViewModel = hiltViewModel()
) {
    val weatherState by weatherVm.state.collectAsStateWithLifecycle()
    val coinsState by coinsVm.state.collectAsStateWithLifecycle()

    // Compose-native permission
    val locationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) weatherVm.startLocationTracking()
        else weatherVm.load(32.0800964, 34.8243129) // fallback TLV
    }
    LaunchedEffect(Unit) { locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION) }

    // Stop location updates when leaving this screen
    DisposableEffect(Unit) { onDispose { weatherVm.stopLocationTracking() } }


        val cells: List<DashCell> = buildList {
            if (weatherState is WeatherViewModel.UiState.Success) {
                val data = (weatherState as WeatherViewModel.UiState.Success).data
                add(
                    WeatherCell(
                        data = data,
                        onClick = { /* TODO: navigate to weather details when added */ }
                    )
                )
            }

            if (coinsState is TopCoinsViewModel.UiState.Success) {
                val coins = (coinsState as TopCoinsViewModel.UiState.Success).coins
                add(
                    TopCoinsCell(
                        coins = coins,
                        onClick = { coinsVm.refresh() }
                    )
                )
            }
        }

        val showInitialLoading = cells.isEmpty() && (
            weatherState is WeatherViewModel.UiState.Idle ||
                weatherState is WeatherViewModel.UiState.Loading ||
                coinsState is TopCoinsViewModel.UiState.Idle ||
                coinsState is TopCoinsViewModel.UiState.Loading
            )

        // LazyColumn that can host any cell type
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(p),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Loading/Idle placeholders
            if (showInitialLoading) {
                item("loading_initial") { LoadingCard() }
            }

            if (weatherState is WeatherViewModel.UiState.Error) {
                val msg = (weatherState as WeatherViewModel.UiState.Error).message
                item("error_weather") {
                    ErrorCard(message = msg, onRetry = { weatherVm.refresh() })
                }
            }

            if (coinsState is TopCoinsViewModel.UiState.Error) {
                val msg = (coinsState as TopCoinsViewModel.UiState.Error).message
                item("error_coins") {
                    ErrorCard(message = msg, onRetry = { coinsVm.refresh() })
                }
            }

            // Real cells
            items(
                items = cells,
                key = { it.key }
            ) { cell ->
                when (cell) {
                    is WeatherCell -> WeatherCard(
                        modifier = Modifier,
                        data = cell.data,
                        onClick = cell.onClick
                    )
                    is TopCoinsCell -> TopCoinsCard(
                        modifier = Modifier,
                        coins = cell.coins,
                        onClick = cell.onClick
                    )
                    // Add more cell renderers here (e.g., NewsCell, TasksCell, BalanceCell, etc.)
                }
            }

            // Example footer actions
            if (weatherState is WeatherViewModel.UiState.Success) {
                item("actions_refresh_weather") {
                    FilledTonalButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { weatherVm.refresh() }
                    ) {
                        Text("Refresh weather")
                    }
                }
            }

            if (coinsState is TopCoinsViewModel.UiState.Success) {
                item("actions_refresh_coins") {
                    FilledTonalButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { coinsVm.refresh() }
                    ) {
                        Text("Refresh coins")
                    }
                }
            }

    }
}

