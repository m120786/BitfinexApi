package com.vb.bitfinexapi.repository

import android.util.Log
import com.vb.bitfinexapi.WebSocketListener
import com.vb.bitfinexapi.model.BookModel
import com.vb.bitfinexapi.model.toBookModel
import com.vb.bitfinexapi.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

class MainRepository {

    fun subscribeToTicker(): WebSocketListener {
        val requestObjTicker = JSONObject()
        requestObjTicker.put("event", Constants.TICKER_SUBSCRIBE)
        requestObjTicker.put("channel", Constants.TICKER_CHANNEL)
        requestObjTicker.put("symbol", Constants.TICKER_SYMBOL)
        val connectionTicker = WebClient()
        return connectionTicker.startSocket(requestObjTicker)
    }

    fun subscribeToBook(): Flow<BookModel?> {
        val requestObjBook = JSONObject()
        requestObjBook.put("event", Constants.BOOK_SUBSCRIBE)
        requestObjBook.put("channel", Constants.BOOK_CHANNEL)
        requestObjBook.put("pair", Constants.BOOK_PAIR)
        requestObjBook.put("prec", Constants.BOOK_PRECISION)
        val connectionBook = WebClient().startSocket(requestObjBook)
        val bookResult = connectionBook.socketOutput.filter {
            it.text!!.startsWith("[") && !JSONArray(it.text).get(1).equals("hb")
        }.map { data ->
            Log.v("WSS", data.toString())
            data.text?.toBookModelJsonArray()?.toBookModel()
        }
        return bookResult
    }

    fun String.toBookModelJsonArray(): ArrayList<String>? {
        var json = JSONTokener(this).nextValue()
        val jsonArrayList: ArrayList<String> = ArrayList()
        var jsonString = this
        when (json) {
            is JSONObject -> {}  // HANDLE OTHER RESPONSES
            is JSONArray -> {
                val jsonArray = JSONArray(jsonString)
                if (jsonArray.get(1).equals("hb")) {
                } else {
                    var bookTicker = JSONArray(jsonString).getJSONArray(1)
                    if (bookTicker.length() > 9) {
                        bookTicker = bookTicker.getJSONArray(1)
                    }
                    for (i in 0 until bookTicker.length()) {
                        val valueString: String = bookTicker.getString(i)
                        jsonArrayList.add(valueString)
                    }
                    Log.v("REP", jsonArrayList.toString())
                }
            }
            else -> {}
        }
        return jsonArrayList
    }
}

