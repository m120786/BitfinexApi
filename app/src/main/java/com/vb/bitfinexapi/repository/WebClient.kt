package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.WebSocketListener
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class WebClient() {

    var webSocketListenerLocal: WebSocketListener? = null

    private val socketHttpClient = OkHttpClient.Builder()
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

   private val request = Request.Builder()
        .url("wss://api-pub.bitfinex.com/ws/2")
        .build()

    fun startSocket(requestObj: JSONObject): WebSocketListener {
        val webSocketListener = WebSocketListener(requestObj.toString())
        webSocketListenerLocal = webSocketListener
        socketHttpClient.newWebSocket(request, webSocketListener)
        return webSocketListener
    }


    }

