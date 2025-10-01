package com.netanel.smartdash.core.network

import com.netanel.smartdash.core.cache.CachePolicy
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CacheControlInterceptor @Inject constructor() : Interceptor {
    private val maxAgeSeconds = CachePolicy.DEFAULT_MAX_AGE_SECONDS

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cacheAwareRequest = if (request.method.equals("GET", ignoreCase = true) &&
            request.header("Cache-Control") == null
        ) {
            request.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAgeSeconds")
                .build()
        } else {
            request
        }

        val response = chain.proceed(cacheAwareRequest)
        return if (
            cacheAwareRequest.method.equals("GET", ignoreCase = true) &&
            response.header("Cache-Control") == null
        ) {
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAgeSeconds")
                .build()
        } else {
            response
        }
    }
}
