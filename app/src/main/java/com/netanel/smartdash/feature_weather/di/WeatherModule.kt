package com.netanel.smartdash.feature_weather.di

import com.netanel.smartdash.feature_weather.data.repo.WeatherRepository
import com.netanel.smartdash.feature_weather.data.repo.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {
    @Provides
    @Singleton
    fun provideWeatherRepo(impl: WeatherRepositoryImpl): WeatherRepository = impl
}
