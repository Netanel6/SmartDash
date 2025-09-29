package com.netanel.smartdash.core.network
import com.netanel.smartdash.core.di.HeadersInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

fun newOkHttp(
    headers: HeadersInterceptor,
    cacheControl: CacheControlInterceptor,
    enableLogging: Boolean,
    cache: Cache? = null
): OkHttpClient {
  val logging = HttpLoggingInterceptor().apply {
    level = if (enableLogging) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
  }
  return OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(15, TimeUnit.SECONDS)
    .writeTimeout(15, TimeUnit.SECONDS)
    .apply { if (cache != null) cache(cache) }
    .addInterceptor(headers)
    .addNetworkInterceptor(cacheControl)
    .addInterceptor(logging)
    .build()
}
