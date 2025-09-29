package com.netanel.smartdash.feature_coins.domain.usecase

import com.netanel.smartdash.core.cache.CachePolicy
import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_coins.data.repo.CoinRepository
import com.netanel.smartdash.feature_coins.domain.model.TopCoin
import javax.inject.Inject

class GetTopCoins @Inject constructor(
    private val repo: CoinRepository
) {
    suspend operator fun invoke(
        limit: Int = 5,
        vsCurrency: String = "usd",
        policy: CachePolicy = CachePolicy.CACHE_THEN_NETWORK
    ): ApiResult<List<TopCoin>> =
        repo.topCoins(limit = limit, vsCurrency = vsCurrency, policy = policy)
}
