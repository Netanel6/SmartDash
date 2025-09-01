package com.netanel.smartdash.core.network

import kotlinx.serialization.json.Json

object JsonProvider {
  val json: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    isLenient = true
  }
}