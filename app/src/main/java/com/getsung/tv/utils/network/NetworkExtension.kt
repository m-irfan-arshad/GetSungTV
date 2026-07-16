package com.getsung.tv.utils.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@Suppress("unused")
fun Context?.isInternetAvailable(): Boolean {
    try {
        val connectivityManager =
            this?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.isNetworkCapabilitiesValid()

    } catch (ex: Exception) {
        ex.printStackTrace()
        return false
    }
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@Suppress("unused")
fun ConnectivityManager.isInternetAvailable(): Boolean {
    try {
        val network = activeNetwork ?: return false
        val capabilities = getNetworkCapabilities(network) ?: return false
        return capabilities.isNetworkCapabilitiesValid()
    } catch (ex: Exception) {
        ex.printStackTrace()
        return false
    }
}

fun NetworkCapabilities?.isNetworkCapabilitiesValid(): Boolean = when {
    this == null -> false
    hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
            (hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) -> true

    else -> false
}