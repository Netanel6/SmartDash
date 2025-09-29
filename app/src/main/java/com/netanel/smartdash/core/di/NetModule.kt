package com.netanel.smartdash.core.di

import android.content.Context
import com.netanel.smartdash.core.network.CacheControlInterceptor
import com.netanel.smartdash.core.network.DynamicApi
import com.netanel.smartdash.core.network.DynamicClient
import com.netanel.smartdash.core.network.JsonProvider
import com.netanel.smartdash.core.network.newOkHttp
import com.netanel.smartdash.core.network.newRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetModule {

    @Provides
    @Singleton
    @UserAgent
    fun userAgent(): String = "SmartDash/NetInfra (Android)"

    @Provides
    @Singleton
    fun tokenStore(): TokenStore = InMemoryTokenStore()

    @Provides
    @Singleton
    fun cacheControlInterceptor(): CacheControlInterceptor = CacheControlInterceptor(300)

    @Provides
    @Singleton
    fun okHttp(
        headers: HeadersInterceptor,
        @ApplicationContext ctx: Context,
        cacheControlInterceptor: CacheControlInterceptor
    ): OkHttpClient = newOkHttp(
        headers = headers,
        enableLogging = true,
        cache = Cache(File(ctx.cacheDir, "http_cache"), 10L * 1024L * 1024L),
        networkInterceptors = listOf(cacheControlInterceptor)
    )

    @Provides
    @Singleton
    fun retrofit(client: OkHttpClient) = newRetrofit(
        baseUrl = "https://placeholder/", // כי נשתמש ב-@Url מלאים
        client = client,
        json = JsonProvider.json
    )

    @Provides
    @Singleton
    fun dynamicApi(retrofit: retrofit2.Retrofit): DynamicApi =
        retrofit.create(DynamicApi::class.java)

    @Provides
    @Singleton
    fun dynamicClient(api: DynamicApi): DynamicClient =
        DynamicClient(api, JsonProvider.json)
}
