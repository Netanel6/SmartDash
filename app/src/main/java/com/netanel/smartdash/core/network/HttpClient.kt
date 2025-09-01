package com.netanel.smartdash.core.network

import javax.inject.Inject

class HttpClient @Inject constructor(
    @PublishedApi internal val dynamic: DynamicClient
) {
    suspend inline fun <reified T> request(req: HttpRequest): ApiResult<T> =
        dynamic.requestTyped(req.method, req.url, req.headers, req.query, req.body)

    suspend fun requestRaw(req: HttpRequest) =
        dynamic.requestRaw(req.method, req.url, req.headers, req.query, req.body)
}
