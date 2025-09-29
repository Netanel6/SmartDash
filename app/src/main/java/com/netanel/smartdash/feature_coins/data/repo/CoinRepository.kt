package com.netanel.smartdash.feature_coins.data.repo

import com.netanel.smartdash.core.cache.CachePolicy
import com.netanel.smartdash.core.database.dao.CoinDao
import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_coins.data.local.toDomain
import com.netanel.smartdash.feature_coins.data.local.toEntity
import com.netanel.smartdash.feature_coins.data.remote.CoinGeckoRemote
import com.netanel.smartdash.feature_coins.domain.mapper.toDomain
import com.netanel.smartdash.feature_coins.domain.model.TopCoin
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

interface CoinRepository {
    suspend fun topCoins(
        limit: Int = 5,
        vsCurrency: String = "usd",
        policy: CachePolicy = CachePolicy.CACHE_THEN_NETWORK
    ): ApiResult<List<TopCoin>>
}

class CoinRepositoryImpl @Inject constructor(
    private val remote: CoinGeckoRemote,
    private val coinDao: CoinDao
) : CoinRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override suspend fun topCoins(
        limit: Int,
        vsCurrency: String,
        policy: CachePolicy
    ): ApiResult<List<TopCoin>> {
        val cached = coinDao.get(limit).map { it.toDomain() }
        return when (policy) {
            CachePolicy.CACHE_ONLY -> {
                if (cached.isNotEmpty()) {
                    ApiResult.Success(cached, 200)
                } else {
                    ApiResult.UnknownError(IllegalStateException("No cached coins available"))
                }
            }

            CachePolicy.CACHE_THEN_NETWORK -> {
                if (cached.isNotEmpty()) {
                    scope.launch { fetchAndStore(limit, vsCurrency) }
                    ApiResult.Success(cached, 200)
                } else {
                    fetchAndStore(limit, vsCurrency)
                }
            }

            CachePolicy.NETWORK_ONLY -> fetchAndStore(limit, vsCurrency)
        }
    }

    private suspend fun fetchAndStore(
        limit: Int,
        vsCurrency: String
    ): ApiResult<List<TopCoin>> {
        return when (val res = remote.topCoins(vsCurrency = vsCurrency, limit = limit)) {
            is ApiResult.Success -> {
                val domain = res.value.toDomain()
                val timestamp = System.currentTimeMillis()
                if (domain.isNotEmpty()) {
                    coinDao.insert(
                        domain.mapIndexed { index, coin ->
                            coin.toEntity(timestamp + index)
                        }
                    )
                }
                ApiResult.Success(domain, res.code)
            }

            is ApiResult.HttpError -> res
            is ApiResult.NetworkError -> res
            is ApiResult.SerializationError -> res
            is ApiResult.UnknownError -> res
        }
    }
}
