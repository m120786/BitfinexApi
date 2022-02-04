package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.WebSocketListener
import com.vb.bitfinexapi.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class Repository {

    init {
        val requestObj= JSONObject()
        requestObj.put("event", Constants.TICKER_EVENT)
        requestObj.put("channel",Constants.TICKER_CHANNEL)
        requestObj.put("symbol",Constants.TICKER_SYMBOL)

        val socketHttpClient = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("wss://api-pub.bitfinex.com/ws/2")
            .build()

        val webSocketListener = WebSocketListener(requestObj.toString())
        socketHttpClient.newWebSocket(request, webSocketListener)
    }
}