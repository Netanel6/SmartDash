package com.netanel.smartdash.app

sealed interface NavRoutes {
    val route: String

    data object Home : NavRoutes { override val route = "home" }
    // data object WeatherDetails : NavRoutes { override val route = "weather/details" } // future
}
