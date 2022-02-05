package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.WebSocketListener
import com.vb.bitfinexapi.utils.Constants
import org.json.JSONObject

class MainRepository {

    fun subscribeToTicker(): WebSocketListener {
        val requestObj = JSONObject()
        requestObj.put("event", Constants.TICKER_EVENT)
        requestObj.put("channel", Constants.TICKER_CHANNEL)
        requestObj.put("symbol", Constants.TICKER_SYMBOL)
        val connection = WebClient()
        return connection.startSocket(requestObj)
    }
}