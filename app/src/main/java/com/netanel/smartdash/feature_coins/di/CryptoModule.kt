package com.netanel.smartdash.feature_coins.di

import com.netanel.smartdash.feature_coins.data.repo.CoinRepository
import com.netanel.smartdash.feature_coins.data.repo.CoinRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CryptoModule {
    @Provides
    @Singleton
    fun provideCoinRepository(impl: CoinRepositoryImpl): CoinRepository = impl
}
