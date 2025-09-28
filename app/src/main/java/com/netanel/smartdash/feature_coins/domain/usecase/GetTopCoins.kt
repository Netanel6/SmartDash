package com.netanel.smartdash.feature_coins.domain.usecase

import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.feature_coins.data.repo.CryptoRepository
import com.netanel.smartdash.feature_coins.domain.model.TopCoin
import javax.inject.Inject

class GetTopCoins @Inject constructor(
    private val repo: CryptoRepository
) {
    suspend operator fun invoke(limit: Int = 5, vsCurrency: String = "usd"): ApiResult<List<TopCoin>> =
        repo.topCoins(limit = limit, vsCurrency = vsCurrency)
}
