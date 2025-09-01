package com.netanel.smartdash.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.netanel.smartdash.core.theme.SmartDashTheme
import com.netanel.smartdash.feature_weather.ui.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartDashTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}*/

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: WeatherViewModel = hiltViewModel()
            val state by vm.state.collectAsState()

            LaunchedEffect(Unit) {
                vm.load(32.0800964, 34.8243129) // Tel Aviv coords
            }

            when (val s = state) {
                is WeatherViewModel.UiState.Idle -> Text("Idle…")
                is WeatherViewModel.UiState.Loading -> Text("Loading…")
                is WeatherViewModel.UiState.Success -> Text(
                    "🌤 ${s.data.city}: ${s.data.temperatureC}°C, ${s.data.description}"
                )
                is WeatherViewModel.UiState.Error -> Text("❌ ${s.message}")
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartDashTheme {
        Greeting("Android")
    }
}