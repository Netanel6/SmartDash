package com.netanel.smartdash.core.network // עדכן לפי החבילה שלך

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import retrofit2.Response
import javax.inject.Inject

enum class HttpMethod { GET, POST, PUT, PATCH, DELETE }

class DynamicClient @Inject constructor(
    private val api: DynamicApi,
    @PublishedApi internal val json: Json = JsonProvider.json
) {
    private fun Map<String, Any?>.toQuery() =
        mapValues { it.value?.toString() ?: "" }

    suspend fun requestRaw(
        method: HttpMethod,
        url: String,
        headers: Map<String, String> = emptyMap(),
        query: Map<String, Any?> = emptyMap(),
        body: JsonElement? = null
    ): ApiResult<JsonElement> = try {
        val r = when (method) {
            HttpMethod.GET -> api.get(url, headers, query.toQuery())
            HttpMethod.POST -> api.post(url, headers, query.toQuery(), body)
            HttpMethod.PUT -> api.put(url, headers, query.toQuery(), body)
            HttpMethod.PATCH -> api.patch(url, headers, query.toQuery(), body)
            HttpMethod.DELETE -> api.delete(url, headers, query.toQuery())
        }
        r.toApiResult()
    } catch (t: Throwable) {
        ApiResult.NetworkError(t)
    }

    public suspend inline fun <reified T> requestTyped(
        method: HttpMethod,
        url: String,
        headers: Map<String, String> = emptyMap(),
        query: Map<String, Any?> = emptyMap(),
        bodyObj: Any? = null
    ): ApiResult<T> {
        val body = bodyObj?.let {
            when (it) {
                is JsonElement -> it
                is String -> runCatching { json.parseToJsonElement(it) }.getOrElse { _: Throwable ->
                    JsonPrimitive(
                        it
                    )
                }

                else -> json.encodeToJsonElement(it)
            }
        }
        return when (val raw = requestRaw(method, url, headers, query, body)) {
            is ApiResult.Success -> runCatching { json.decodeFromJsonElement<T>(raw.value) }
                .fold(
                    onSuccess = { ApiResult.Success(it, raw.code) },
                    onFailure = { ApiResult.SerializationError(it, raw.value.toString()) }
                )

            is ApiResult.HttpError -> raw
            is ApiResult.NetworkError -> raw
            is ApiResult.SerializationError -> raw
            is ApiResult.UnknownError -> raw
        }
    }
}

private fun Response<JsonElement>.toApiResult(): ApiResult<JsonElement> = try {
    if (isSuccessful) ApiResult.Success(body() ?: JsonNull, code())
    else ApiResult.HttpError(code(), errorBody()?.string())
} catch (t: Throwable) {
    ApiResult.UnknownError(t)
}
