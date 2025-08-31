package com.netanel.smartdash.domain

import kotlinx.serialization.json.JsonElement
import retrofit2.Response
import retrofit2.http.*

interface DynamicApi {
  @GET
  suspend fun get(
    @Url url: String,
    @HeaderMap headers: Map<String, String> = emptyMap(),
    @QueryMap(encoded = true) query: Map<String, String> = emptyMap()
  ): Response<JsonElement>

  @POST
  suspend fun post(
    @Url url: String,
    @HeaderMap headers: Map<String, String> = emptyMap(),
    @QueryMap(encoded = true) query: Map<String, String> = emptyMap(),
    @Body body: JsonElement? = null
  ): Response<JsonElement>

  @PUT
  suspend fun put(
    @Url url: String,
    @HeaderMap headers: Map<String, String> = emptyMap(),
    @QueryMap(encoded = true) query: Map<String, String> = emptyMap(),
    @Body body: JsonElement? = null
  ): Response<JsonElement>

  @PATCH
  suspend fun patch(
    @Url url: String,
    @HeaderMap headers: Map<String, String> = emptyMap(),
    @QueryMap(encoded = true) query: Map<String, String> = emptyMap(),
    @Body body: JsonElement? = null
  ): Response<JsonElement>

  @DELETE
  suspend fun delete(
    @Url url: String,
    @HeaderMap headers: Map<String, String> = emptyMap(),
    @QueryMap(encoded = true) query: Map<String, String> = emptyMap()
  ): Response<JsonElement>
}
