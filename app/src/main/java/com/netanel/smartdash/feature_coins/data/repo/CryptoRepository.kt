package com.netanel.smartdash.feature_coins.data.repo

import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_coins.data.remote.CoinGeckoRemote
import com.netanel.smartdash.feature_coins.domain.mapper.toDomain
import com.netanel.smartdash.feature_coins.domain.model.TopCoin
import javax.inject.Inject

interface CryptoRepository {
    suspend fun topCoins(limit: Int = 5, vsCurrency: String = "usd"): ApiResult<List<TopCoin>>
}

class CryptoRepositoryImpl @Inject constructor(
    private val remote: CoinGeckoRemote
) : CryptoRepository {
    override suspend fun topCoins(limit: Int, vsCurrency: String): ApiResult<List<TopCoin>> {
        return when (val res = remote.topCoins(vsCurrency = vsCurrency, limit = limit)) {
            is ApiResult.Success -> ApiResult.Success(res.value.toDomain(), res.code)
            is ApiResult.HttpError -> res
            is ApiResult.NetworkError -> res
            is ApiResult.SerializationError -> res
            is ApiResult.UnknownError -> res
        }
    }
}
