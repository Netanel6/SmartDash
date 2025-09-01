package com.netanel.smartdash.core.di

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Qualifier

interface TokenStore { fun get(): String?; fun set(token: String?) }

class InMemoryTokenStore @Inject constructor(): TokenStore {
    @Volatile private var t: String? = null
    override fun get(): String? = t
    override fun set(token: String?) { t = token }
}

@Qualifier
annotation class UserAgent

class HeadersInterceptor @Inject constructor(
    private val tokenStore: TokenStore,
    @UserAgent private val userAgent: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val b = chain.request().newBuilder()
            .header("Accept", "application/json")
            .header("User-Agent", userAgent)
        tokenStore.get()?.let { b.header("Authorization", "Bearer $it") }
        return chain.proceed(b.build())
    }
}