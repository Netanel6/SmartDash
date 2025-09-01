package com.netanel.smartdash.app

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.smartdash.core.permissions.PermissionManager
import com.netanel.smartdash.core.permissions.PermissionResult
import com.netanel.smartdash.core.theme.SmartDashTheme
import com.netanel.smartdash.feature_weather.ui.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var pm: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pm = PermissionManager.from(this) // ✅ registers the launcher here

        setContent {
            val vm: WeatherViewModel = hiltViewModel()
            val state by vm.state.collectAsState()

            LaunchedEffect(Unit) {
                when (val res = pm.request(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    PermissionResult.Granted -> vm.load(32.0800964, 34.8243129) // later replace with real coords
                    is PermissionResult.Denied -> vm.load(32.0800964, 34.8243129)
                    PermissionResult.DeniedPermanently -> vm.load(32.0800964, 34.8243129)
                }
            }

            SmartDashTheme {
                when (val s = state) {
                    is WeatherViewModel.UiState.Idle -> Text("Idle…")
                    is WeatherViewModel.UiState.Loading -> Text("Loading…")
                    is WeatherViewModel.UiState.Success ->
                        Text("🌤 ${s.data.city}: ${s.data.temperatureC}°C, ${s.data.description}")
                    is WeatherViewModel.UiState.Error -> Text("❌ ${s.message}")
                }
            }
        }
    }
}
