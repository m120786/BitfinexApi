package com.vb.bitfinexapi.connection

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkConnection(context: Context) {

    val connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Unavailable)

    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    fun getCurrentState(): ConnectionState {
        val actNetwork = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?: return ConnectionState.Unavailable
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
                Log.e("network", "available: " + network)
            }

            override fun onLost(network: Network) {
                val latest = getCurrentState()
                Log.e(
                    "network",
                    "lost " + network
                )
                when (latest) {
                    ConnectionState.Unavailable -> connectionState.value = ConnectionState.Unavailable
                    ConnectionState.Available -> connectionState.value = ConnectionState.Reconnecting
                }
            }

            override fun onUnavailable() {
                connectionState.value = ConnectionState.Unavailable
            }
        })
    }
}
