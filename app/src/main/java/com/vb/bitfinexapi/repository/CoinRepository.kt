package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.model.domainModel.BookModel
import com.vb.bitfinexapi.model.domainModel.TickerModel
import com.vb.bitfinexapi.model.domainModel.toBookModel
import com.vb.bitfinexapi.model.domainModel.toTickerModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

class CoinRepository(val webClient: WebClient): CoinService {

    override fun subscribeToTicker(): Flow<TickerModel?> {
        val requestObjTicker = JSONObject()
        requestObjTicker.put("event", TICKER_SUBSCRIBE)
        requestObjTicker.put("channel", TICKER_CHANNEL)
        requestObjTicker.put("symbol", TICKER_SYMBOL)
        val connectionTicker = webClient.startSocket(requestObjTicker)
        val tickerResult = connectionTicker.socketOutput.filter {
            it.text?.startsWith("[") == true && !JSONArray(it.text).get(1).equals("hb")
        }.map { data ->
            data.text?.toTickerModelJsonArray()?.toTickerModel()
        }
        return tickerResult
    }

    override fun subscribeToBook(): Flow<BookModel?> {
        val requestObjBook = JSONObject()
        requestObjBook.put("event", BOOK_SUBSCRIBE)
        requestObjBook.put("channel", BOOK_CHANNEL)
        requestObjBook.put("pair", BOOK_PAIR)
        requestObjBook.put("prec", BOOK_PRECISION)

        val connectionBook = webClient.startSocket(requestObjBook)
        val bookResult = connectionBook.socketOutput.filter {
            it.text?.startsWith("[") == true && !JSONArray(it.text).get(1).equals("hb")
        }.map { data ->
//            Log.v("WSS", data.toString())
            data.text?.toBookModelJsonArray()?.toBookModel()
        }
        return bookResult
    }

    override fun unsubscribeTicker() {
        TODO("Not yet implemented")
    }

    override fun unsubscribeBook() {
        TODO("Not yet implemented")
    }

    fun String.toBookModelJsonArray(): ArrayList<String>? {
        var json = JSONTokener(this).nextValue()
        val jsonArrayList: ArrayList<String> = ArrayList()
        var jsonString = this
        when (json) {
            is JSONObject -> {}  // HANDLE OTHER RESPONSES
            is JSONArray -> {
                    var bookTicker = JSONArray(jsonString).getJSONArray(1)
                    if (bookTicker.length() > 9) {
                        bookTicker = bookTicker.getJSONArray(1)
                    }
                    for (i in 0 until bookTicker.length()) {
                        val valueString: String = bookTicker.getString(i)
                        jsonArrayList.add(valueString)
                    }
            }
            else -> {}
        }
        return jsonArrayList
    }

    fun String.toTickerModelJsonArray(): ArrayList<String> {
        var json = JSONTokener(this).nextValue()
        val jsonArrayList: ArrayList<String> = ArrayList()
        var jsonString = this
        when (json) {
            is JSONObject -> {}
            is JSONArray -> {
                var jsonTicker = JSONArray(jsonString).getJSONArray(1)
                if (jsonTicker.length() > 3 && jsonTicker.length() < 11) {
                    for (i in 0 until jsonTicker.length()) {
                        val valueString: String = jsonTicker.getString(i)
                        jsonArrayList.add(valueString)
                    }
                }
            }
        }
        return jsonArrayList
    }

    companion object {
        const val TICKER_SUBSCRIBE = "subscribe"
        const val TICKER_CHANNEL = "ticker"
        const val TICKER_SYMBOL = "tBTCUSD"

        const val BOOK_SUBSCRIBE = "subscribe"
        const val BOOK_CHANNEL = "book"
        const val BOOK_PAIR = "BTCUSD"
        const val BOOK_PRECISION = "P2"

        const val BOOK_SIZE = 10
    }
}


