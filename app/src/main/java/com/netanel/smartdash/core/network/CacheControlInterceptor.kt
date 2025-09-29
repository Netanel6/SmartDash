package com.netanel.smartdash.core.network

import okhttp3.Interceptor
import okhttp3.Response

class CacheControlInterceptor(
    private val maxAgeSeconds: Int
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", "public, max-age=$maxAgeSeconds")
            .build()
    }
}
