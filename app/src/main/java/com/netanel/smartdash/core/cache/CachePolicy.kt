package com.netanel.smartdash.core.cache

/**
 * Defines how repositories should interact with cached data versus the network.
 */
enum class CachePolicy {
    NETWORK_ONLY,
    CACHE_ONLY,
    CACHE_THEN_NETWORK;

    val shouldReadFromCache: Boolean
        get() = this != NETWORK_ONLY

    val shouldFetchFromNetwork: Boolean
        get() = this != CACHE_ONLY

    /**
     * Maps the policy to Cache-Control request headers so OkHttp's HTTP cache
     * can honour the desired behaviour.
     */
    fun toRequestHeaders(maxAgeSeconds: Int = DEFAULT_MAX_AGE_SECONDS): Map<String, String> {
        val value = when (this) {
            NETWORK_ONLY -> "no-cache"
            CACHE_ONLY -> "only-if-cached,max-stale=$maxAgeSeconds"
            CACHE_THEN_NETWORK -> "max-age=0"
        }
        return mapOf("Cache-Control" to value)
    }

    companion object {
        const val DEFAULT_MAX_AGE_SECONDS: Int = 5 * 60
    }
}
