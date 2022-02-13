package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.model.domainModel.BookModel
import com.vb.bitfinexapi.model.domainModel.TickerModel
import com.vb.bitfinexapi.model.domainModel.toBookModel
import com.vb.bitfinexapi.model.domainModel.toTickerModel
import com.vb.bitfinexapi.model.networkModel.SocketData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

class CoinRepository(private val webClient: WebClient) : CoinService {

    override fun subscribeToTicker(requestObjTicker: JSONObject): Flow<TickerModel?> {
        webClient.startSocket(requestObjTicker)
        val tickerResult = webClient.socketOutput.filter { socketText ->
            socketText.text?.startsWith("{") == false && !JSONArray(socketText.text).get(1)
                .equals("hb") && JSONArray(socketText.text).getJSONArray(1).length() > 3 &&
                    JSONArray(socketText.text).getJSONArray(1).length() < 11
        }.map { data ->
            data.text?.toTickerModelJsonArray()?.toTickerModel()
        }
        return tickerResult
    }

    override fun subscribeToBook(requestObjBook: JSONObject): Flow<BookModel?> {
        webClient.startSocket(requestObjBook)
        val bookResult = webClient.socketOutput.filter { socketText ->
            socketText.text?.startsWith("{") == false && !JSONArray(socketText.text).get(1)
                .equals("hb") && JSONArray(socketText.text).getJSONArray(1).length() < 4
        }.map { data ->
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

    override fun subscribeToError(): Flow<Throwable?> {
       var exception = webClient.socketOutput.filter {
        it.exception != null
          }.map {
            it.exception
        }
        return exception
    }

    private fun String.toBookModelJsonArray(): ArrayList<String> {
        val json = JSONTokener(this).nextValue()
        val jsonArrayList: ArrayList<String> = ArrayList()
        val jsonString = this
        when (json) {
            is JSONObject -> {}  // HANDLE OTHER RESPONSES
            is JSONArray -> {
                var bookTicker = JSONArray(jsonString).getJSONArray(1)
                if (bookTicker.length() > 9) {
                    bookTicker = bookTicker.getJSONArray(1)
                }
                for (i in 0 until bookTicker.length()) {
                    val valueString: String = bookTicker.get(i).toString()
                    jsonArrayList.add(valueString)
                }
            }
            else -> {}
        }
        return jsonArrayList
    }

    private fun String.toTickerModelJsonArray(): ArrayList<String> {
        val json = JSONTokener(this).nextValue()
        val jsonArrayList: ArrayList<String> = ArrayList()
        val jsonString = this
        when (json) {
            is JSONObject -> {}
            is JSONArray -> {
                val jsonTicker = JSONArray(jsonString).getJSONArray(1)
                if (jsonTicker.length() in 4..10) {
                    for (i in 0 until jsonTicker.length()) {
                        val valueString: String = jsonTicker.get(i).toString()
                        jsonArrayList.add(valueString)
                    }
                }
            }
        }
        return jsonArrayList
    }


}


