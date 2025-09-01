package com.netanel.smartdash.weather


fun YahooWeatherResponse.toDomain(): WeatherNow =
    WeatherNow(
        city = location?.city.orEmpty(),
        temperatureC = current_observation?.condition?.temperature,
        description = current_observation?.condition?.text
    )
