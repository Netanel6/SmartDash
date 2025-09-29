package com.netanel.smartdash.feature_coins.data.repo

import com.netanel.smartdash.core.cache.CachePolicy
import com.netanel.smartdash.core.database.CoinCacheEntity
import com.netanel.smartdash.core.database.CoinDao
import com.netanel.smartdash.core.database.toDomain
import com.netanel.smartdash.core.database.toEntity
import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_coins.data.remote.CoinGeckoRemote
import com.netanel.smartdash.feature_coins.domain.mapper.toDomain
import com.netanel.smartdash.feature_coins.domain.model.TopCoin
import javax.inject.Inject

interface CryptoRepository {
    suspend fun topCoins(
        limit: Int = 5,
        vsCurrency: String = "usd",
        policy: CachePolicy = CachePolicy.CACHE_THEN_NETWORK
    ): ApiResult<List<TopCoin>>
}

class CryptoRepositoryImpl @Inject constructor(
    private val remote: CoinGeckoRemote,
    private val coinDao: CoinDao
) : CryptoRepository {
    override suspend fun topCoins(
        limit: Int,
        vsCurrency: String,
        policy: CachePolicy
    ): ApiResult<List<TopCoin>> {
        val queryKey = queryKey(limit, vsCurrency)
        val cached = if (policy.shouldReadFromCache) coinDao.getCoins(queryKey) else emptyList()

        if (!policy.shouldFetchFromNetwork) {
            return if (cached.isNotEmpty()) {
                ApiResult.Success(cached.map { it.toDomain() }, 200)
            } else {
                ApiResult.HttpError(404, "No cached coin data")
            }
        }

        return when (val res = remote.topCoins(
            vsCurrency = vsCurrency,
            limit = limit,
            headers = policy.toRequestHeaders()
        )) {
            is ApiResult.Success -> {
                val domain = res.value.toDomain()
                val timestamp = System.currentTimeMillis()
                coinDao.replaceForQuery(
                    queryKey,
                    domain.mapIndexed { index, coin ->
                        coin.toEntity(queryKey, index, timestamp)
                    }
                )
                ApiResult.Success(domain, res.code)
            }

            is ApiResult.HttpError -> cached.toResultOr(res)
            is ApiResult.NetworkError -> cached.toResultOr(res)
            is ApiResult.SerializationError -> cached.toResultOr(res)
            is ApiResult.UnknownError -> cached.toResultOr(res)
        }
    }

    private fun queryKey(limit: Int, vsCurrency: String): String =
        "$vsCurrency|$limit"

    private fun List<CoinCacheEntity>.toResultOr(
        fallback: ApiResult<List<TopCoin>>
    ): ApiResult<List<TopCoin>> {
        return if (isNotEmpty()) {
            ApiResult.Success(map { it.toDomain() }, 200)
        } else {
            fallback
        }
    }
}
