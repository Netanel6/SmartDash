package com.netanel.smartdash.app

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
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
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow
import com.netanel.smartdash.feature_weather.ui.WeatherCard
import com.netanel.smartdash.feature_weather.ui.WeatherViewModel

/* ---------- Dashboard cell models ---------- */
sealed interface DashCell { val key: String }

/** Weather cell (first cell type) */
data class WeatherCell(
    val data: WeatherNow,
    val onClick: () -> Unit
) : DashCell {
    override val key: String = "cell_weather"
}

/* ---------- Screen ---------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartDashScreen(
    vm: WeatherViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    // Compose-native permission
    val locationPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) vm.startLocationTracking()
        else vm.load(32.0800964, 34.8243129) // fallback TLV
    }
    LaunchedEffect(Unit) { locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION) }

    // Stop location updates when leaving this screen
    DisposableEffect(Unit) { onDispose { vm.stopLocationTracking() } }

    Scaffold(
        topBar = { SmallTopAppBar(title = { Text("SmartDash") }) }
    ) { p ->
        // Build cells for the list (for now, only weather)
        val cells: List<DashCell> = when (val s = state) {
            is WeatherViewModel.UiState.Success -> listOf(
                WeatherCell(
                    data = s.data,
                    onClick = { /* TODO: navigate to weather details when added */ }
                )
            )
            is WeatherViewModel.UiState.Error -> listOf(
                // you could add a dedicated ErrorCell later; for now render as a single item via when()
            )
            else -> emptyList()
        }

        // LazyColumn that can host any cell type
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(p),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Loading/Idle placeholders
            when (state) {
                is WeatherViewModel.UiState.Idle,
                is WeatherViewModel.UiState.Loading -> {
                    item("loading") { LoadingCard() }
                }
                is WeatherViewModel.UiState.Error -> {
                    val msg = (state as WeatherViewModel.UiState.Error).message
                    item("error") {
                        ErrorCard(message = msg, onRetry = { vm.refresh() }) }
                }
                else -> Unit
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
                    // Add more cell renderers here (e.g., NewsCell, TasksCell, BalanceCell, etc.)
                }
            }

            // Example footer actions
            if (state is WeatherViewModel.UiState.Success) {
                item("actions_refresh") {
                    OutlinedButton(onClick = { vm.refresh() }) { Text("Refresh") }
                }
            }
        }
    }
}

