package com.netanel.smartdash.feature_coins.data.remote

import com.netanel.smartdash.core.network.ApiResult
import com.netanel.smartdash.core.network.HttpClient
import com.netanel.smartdash.core.network.HttpMethod
import com.netanel.smartdash.core.network.HttpRequest
import com.netanel.smartdash.feature_coins.domain.dto.CoinMarketDto
import javax.inject.Inject

class CoinGeckoRemote @Inject constructor(
    private val http: HttpClient
) {
    suspend fun topCoins(
        vsCurrency: String = "usd",
        limit: Int = 5,
        order: String = "market_cap_desc"
    ): ApiResult<List<CoinMarketDto>> {
        val req = HttpRequest(
            method = HttpMethod.GET,
            url = "https://api.coingecko.com/api/v3/coins/markets",
            query = mapOf(
                "vs_currency" to vsCurrency,
                "order" to order,
                "per_page" to limit,
                "page" to 1,
                "sparkline" to false,
                "price_change_percentage" to "24h"
            )
        )
        return http.request(req)
    }
}
