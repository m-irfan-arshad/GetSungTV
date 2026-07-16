package com.getsung.tv.utils.network

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityMonitor @Inject constructor(@ApplicationContext context: Context) :
    ConnectivityObserver {
    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

//    private val telephonyManager: TelephonyManager by lazy {
//        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//    }

    override fun observe(): Flow<NetworkStatus> = callbackFlow {
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        suspend fun emitNetworkStatus() {
            val network = connectivityManager.activeNetwork

            if (network == null) {
                trySend(NetworkStatus.OnDisconnected)
                return
            }
            val hasInternet = connectivityManager.isInternetAvailable()

            if (!hasInternet) {
                trySend(NetworkStatus.OnDisconnected)
                return
            }
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            if (capabilities == null) {
                trySend(NetworkStatus.OnDisconnected)
                return
            }

            val download = capabilities.linkDownstreamBandwidthKbps
            val upload = capabilities.linkUpstreamBandwidthKbps
            val speed = when {
                download >= 20_000 -> InternetSpeed.FAST      // ≥ 20 Mbps
                download >= 5_000 -> InternetSpeed.MEDIUM     // 5–20 Mbps
                else -> InternetSpeed.SLOW
            }
            val connectionType = when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                    ConnectionType.WIFI

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                    ConnectionType.CELLULAR

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->
                    ConnectionType.ETHERNET

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ->
                    ConnectionType.VPN

                else -> ConnectionType.UNKNOWN
            }
            trySend(
                NetworkStatus.OnConnected(
                    type = connectionType,
                    download,
                    upload,
                    speed = speed
                )
            )
        }

        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                launch {
                    emitNetworkStatus()
                }
            }

            override fun onLost(network: Network) {
                trySend(NetworkStatus.OnDisconnected)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                launch {
                    emitNetworkStatus()
                }
            }
        }
        connectivityManager.registerNetworkCallback(getNetworkRequest(), callback)


    }.distinctUntilChanged()


    private fun getNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
    }
}