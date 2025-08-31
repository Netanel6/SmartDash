package com.netanel.smartdash.domain

sealed class ApiResult<out T> {
  data class Success<T>(val value: T, val code: Int) : ApiResult<T>()
  data class HttpError(val code: Int, val body: String?) : ApiResult<Nothing>()
  data class NetworkError(val throwable: Throwable) : ApiResult<Nothing>()
  data class SerializationError(val throwable: Throwable, val raw: String?) : ApiResult<Nothing>()
  data class UnknownError(val throwable: Throwable) : ApiResult<Nothing>()
}