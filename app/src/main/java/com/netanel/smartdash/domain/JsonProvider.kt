package com.netanel.smartdash.domain

import kotlinx.serialization.json.Json

object JsonProvider {
  val json: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    isLenient = true
  }
}