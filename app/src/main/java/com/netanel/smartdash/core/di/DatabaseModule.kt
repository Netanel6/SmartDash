package com.netanel.smartdash.core.di

import android.content.Context
import androidx.room.Room
import com.netanel.smartdash.core.database.CoinDao
import com.netanel.smartdash.core.database.SmartDashDatabase
import com.netanel.smartdash.core.database.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SmartDashDatabase =
        Room.databaseBuilder(context, SmartDashDatabase::class.java, "smartdash.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideWeatherDao(db: SmartDashDatabase): WeatherDao = db.weatherDao()

    @Provides
    fun provideCoinDao(db: SmartDashDatabase): CoinDao = db.coinDao()
}
