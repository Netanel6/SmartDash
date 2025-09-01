package com.netanel.smartdash.core.permissions

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CompletableDeferred

class PermissionManager private constructor(
    private val activity: ComponentActivity
) {
    companion object {
        fun from(activity: ComponentActivity): PermissionManager = PermissionManager(activity)
    }

    private var pending = CompletableDeferred<PermissionResult>()
    private var lastPermission: String? = null

    // ✅ Register launcher once in onCreate
    private val singleLauncher: ActivityResultLauncher<String> =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            val permission = lastPermission
            val result = when {
                granted -> PermissionResult.Granted
                permission != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) ->
                    PermissionResult.Denied(true)
                else -> PermissionResult.DeniedPermanently
            }
            if (!pending.isCompleted) pending.complete(result)
        }

    suspend fun request(permission: String): PermissionResult {
        // fast path
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            return PermissionResult.Granted
        }

        lastPermission = permission
        val deferred = CompletableDeferred<PermissionResult>()
        pending = deferred
        singleLauncher.launch(permission)
        return deferred.await()
    }
}
