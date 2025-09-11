package com.netanel.smartdash.app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.netanel.smartdash.feature_weather.ui.SmartDashScreen
import com.netanel.smartdash.feature_weather.ui.WeatherViewModel

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = NavRoutes.Home.route) {

        composable(route = NavRoutes.Home.route) {
            // Home dashboard holding weather (for now)
            val vm: WeatherViewModel = hiltViewModel()
            SmartDashScreen(vm = vm /*, onWeatherClick = { navController.navigate(NavRoutes.WeatherDetails.route) } */)
        }

        // composable(NavRoutes.WeatherDetails.route) { WeatherDetailsScreen(...) }
    }
}
