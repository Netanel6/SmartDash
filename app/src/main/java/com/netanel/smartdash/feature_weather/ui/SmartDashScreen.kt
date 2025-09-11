@file:OptIn(ExperimentalMaterial3Api::class)

package com.netanel.smartdash.feature_weather.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.netanel.smartdash.feature_weather.domain.model.WeatherNow

/* ---------- Dashboard cell models ---------- */
sealed interface DashCell { val key: String }

/** Weather cell (first cell type) */
data class WeatherCell(
    val data: WeatherNow,
    val onClick: () -> Unit
) : DashCell {
    override val key: String = "cell_weather" // stable key; keep unique per type/instance
}

/* ---------- Screen ---------- */
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
                    item("error") { ErrorCard(message = msg, onRetry = { vm.refresh() }) }
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

/* ---------- Cards ---------- */

@Composable
private fun LoadingCard() {
    Card(shape = MaterialTheme.shapes.medium) {
        // simple centered loader; you can replace with shimmer later
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun ErrorCard(message: String, onRetry: () -> Unit) {
    Card(shape = MaterialTheme.shapes.medium) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Oops: $message", style = MaterialTheme.typography.bodyMedium)
            OutlinedButton(onClick = onRetry) { Text("Try again") }
        }
    }
}

@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    data: WeatherNow,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick // ✅ use Card's onClick instead of Modifier.clickable
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = data.city,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${data.temperatureC ?: "-"}°C",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = data.description.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

