package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.WebSocketListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class WebClient() {

//    private var webSocketListenerLocal: WebSocketListener? = null
    companion object {
        private val socketHttpClient = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()
         private var webSocketLocal: WebSocket? = null

}
   private val request = Request.Builder()
        .url("wss://api-pub.bitfinex.com/ws/2")
        .build()

    fun startSocket(requestObj: JSONObject): WebSocketListener {
        val webSocketListener = WebSocketListener(requestObj.toString())
        webSocketLocal = socketHttpClient.newWebSocket(request, webSocketListener)
        return webSocketListener
    }
    fun stopSocket() {
        webSocketLocal?.close(1000, "close")
    }


    }

