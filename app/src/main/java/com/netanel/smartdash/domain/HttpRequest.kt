package com.netanel.smartdash.domain

import kotlinx.serialization.json.JsonElement

data class HttpRequest(
    val method: HttpMethod,
    val url: String,
    val headers: Map<String, String> = emptyMap(),
    val query: Map<String, Any?> = emptyMap(),
    val body: JsonElement? = null
)
