package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.SocketData
import com.vb.bitfinexapi.WebSocketListener
import com.vb.bitfinexapi.utils.Constants
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.Socket
import java.util.concurrent.TimeUnit

class DataProvider {

    private var webSocketListener: WebSocketListener? = null

    private val socketHttpClient = OkHttpClient.Builder()
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

   private val request = Request.Builder()
        .url("wss://api-pub.bitfinex.com/ws/2")
        .build()

    fun startSocket() {
        val requestObj = JSONObject()
        requestObj.put("event", Constants.TICKER_EVENT)
        requestObj.put("channel", Constants.TICKER_CHANNEL)
        requestObj.put("symbol", Constants.TICKER_SYMBOL)

        val webSocketListener = WebSocketListener(requestObj.toString())
        socketHttpClient.newWebSocket(request, webSocketListener)
    }
    }

