package com.netanel.smartdash.feature_coins.di

import com.netanel.smartdash.feature_coins.data.repo.CryptoRepository
import com.netanel.smartdash.feature_coins.data.repo.CryptoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CryptoModule {
    @Provides
    fun provideCryptoRepository(impl: CryptoRepositoryImpl): CryptoRepository = impl
}
