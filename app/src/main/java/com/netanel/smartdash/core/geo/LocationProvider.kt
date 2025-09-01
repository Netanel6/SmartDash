package com.netanel.smartdash.core.geo

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.internal.wait

class LocationProvider(context: Context) {

    private val client = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission") // caller must check permission first!
    fun locationUpdates(
        intervalMs: Long = 10_000,
        fastestMs: Long = 5_000
    ): Flow<Location> = callbackFlow {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, intervalMs
        ).setMinUpdateIntervalMillis(fastestMs).build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { trySend(it) }
            }
        }

        client.requestLocationUpdates(request, callback, null)

        awaitClose { client.removeLocationUpdates(callback) }
    }

    @SuppressLint("MissingPermission")
    suspend fun lastLocationOrNull(): Unit = client.lastLocation.wait()
}
