package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.WebSocketListener
import com.vb.bitfinexapi.utils.Constants
import org.json.JSONObject

class MainRepository {

    fun subscribeToTicker(): WebSocketListener {
        val requestObjTicker = JSONObject()
        requestObjTicker.put("event", Constants.TICKER_SUBSCRIBE)
        requestObjTicker.put("channel", Constants.TICKER_CHANNEL)
        requestObjTicker.put("symbol", Constants.TICKER_SYMBOL)
        val connectionTicker = WebClient()
        return connectionTicker.startSocket(requestObjTicker)
    }

    fun subscribeToBook(): WebSocketListener {
        val requestObjBook = JSONObject()
        requestObjBook.put("event", Constants.BOOK_SUBSCRIBE)
        requestObjBook.put("channel", Constants.BOOK_CHANNEL)
        requestObjBook.put("pair", Constants.BOOK_PAIR)
        requestObjBook.put("prec", Constants.BOOK_PRECISION)
        val connectionBook = WebClient()
        return connectionBook.startSocket(requestObjBook)
    }

}