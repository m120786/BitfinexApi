package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.SocketData
import com.vb.bitfinexapi.utils.Constants
import kotlinx.coroutines.flow.MutableSharedFlow
import org.json.JSONObject

class MainRepository {

    fun subscribeToTicker(): MutableSharedFlow<SocketData> {
        val requestObj = JSONObject()
        requestObj.put("event", Constants.TICKER_EVENT)
        requestObj.put("channel", Constants.TICKER_CHANNEL)
        requestObj.put("symbol", Constants.TICKER_SYMBOL)
        val connection = WebClient()
        return connection.startSocket(requestObj).socketOutput
    }
}