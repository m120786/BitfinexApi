package com.vb.bitfinexapi.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkConnection(context: Context) {

    val connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Available)


    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    fun getCurrentState(): ConnectionState {
        val actNetwork = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (actNetwork == null) {
                return ConnectionState.Unavailable
            }
        return when {
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionState.Available
            actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionState.Available
            else -> ConnectionState.Unavailable

        }
    }

    fun registerForConnectionState() {
        connectivityManager.registerNetworkCallback(networkRequest, object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                connectionState.value = ConnectionState.Available
                Log.e("network2", "available: $network")
            }

            override fun onLost(network: Network) {
                val latest = getCurrentState()
                Log.e(
                    "network2",
                    "lost $network"
                )
                when (latest) {
                    ConnectionState.Unavailable -> connectionState.value =
                        ConnectionState.Unavailable
                    ConnectionState.Available -> connectionState.value =
                        ConnectionState.Reconnecting
                }
                Log.e(
                    "network2",
                    connectionState.value.toString()
                )
            }

            override fun onUnavailable() {
                connectionState.value = ConnectionState.Unavailable
                Log.e("network2", "not available")

            }
        })
    }
}