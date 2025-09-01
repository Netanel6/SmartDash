package com.netanel.smartdash.core.permissions

sealed class PermissionResult {
    data object Granted : PermissionResult()
    data class Denied(val shouldShowRationale: Boolean) : PermissionResult()
    data object DeniedPermanently : PermissionResult()
}

data class MultiplePermissionResult(
    val granted: Set<String>,
    val denied: Set<String>,
    val deniedPermanently: Set<String>
) {
    val allGranted: Boolean get() = denied.isEmpty() && deniedPermanently.isEmpty()
}
