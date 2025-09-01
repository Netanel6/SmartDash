package com.netanel.smartdash.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

fun newRetrofit(
  baseUrl: String,
  client: OkHttpClient,
  json: Json = JsonProvider.json
): Retrofit = Retrofit.Builder()
  .baseUrl(baseUrl) // חייב להסתיים ב-"/"
  .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
  .client(client)
  .build()
