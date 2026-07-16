package com.getsung.tv.utils.network

sealed class NetworkStatus {
    object NAN : NetworkStatus()
    object OnDisconnected : NetworkStatus()

    data class OnConnected(
        val type: ConnectionType,
        val downloadBandwidthKbps: Int,
        val uploadBandwidthKbps: Int,
        val speed: InternetSpeed
    ) : NetworkStatus()
}

enum class ConnectionType {
    WIFI,
    ETHERNET,
    CELLULAR,
    VPN,
    UNKNOWN
}

enum class InternetSpeed {
    OFFLINE,
    SLOW,
    MEDIUM,
    FAST
}