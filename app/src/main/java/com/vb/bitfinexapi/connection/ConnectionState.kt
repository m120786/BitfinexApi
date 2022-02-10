package com.vb.bitfinexapi.connection

sealed class ConnectionState {
    object Available: ConnectionState()
    object Unavailable: ConnectionState()
    object Reconnecting: ConnectionState()
}
