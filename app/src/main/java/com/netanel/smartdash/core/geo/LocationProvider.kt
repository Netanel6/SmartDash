package com.netanel.smartdash.core.geo


import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationProvider(context: Context) {

    private val client = LocationServices.getFusedLocationProviderClient(context)

    /**
     * Poll location every [intervalMs] and emit it.
     * Default = every 6 minutes (360_000 ms).
     */
    @SuppressLint("MissingPermission")
    fun pollLocation(intervalMs: Long = 360_000): Flow<Location?> = flow {
        while (true) {
            emit(lastLocationOrNull())
            delay(intervalMs)
        }
    }

    /**
     * One-shot last known location.
     */
    @SuppressLint("MissingPermission")
    suspend fun lastLocationOrNull(): Location? =
        suspendCancellableCoroutine { cont ->
            client.lastLocation
                .addOnSuccessListener { cont.resume(it) }
                .addOnFailureListener { cont.resume(null) }
            cont.invokeOnCancellation { /* no-op */ }
        }
}
