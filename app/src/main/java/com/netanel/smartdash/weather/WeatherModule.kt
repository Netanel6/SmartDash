package com.netanel.smartdash.weather

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {
    @Provides
    fun provideWeatherRepo(impl: WeatherRepositoryImpl): WeatherRepository = impl
}
