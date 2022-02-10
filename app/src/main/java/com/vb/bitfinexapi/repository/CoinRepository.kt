package com.vb.bitfinexapi.repository

import android.util.Log
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

    override fun subscribeToTicker(requestObjTicker: JSONObject): Flow<TickerModel?> {
        webClient.startSocket(requestObjTicker)
        val tickerResult = webClient.socketOutput.filter { socketText ->
            socketText.text?.startsWith("{") == false && !JSONArray(socketText.text).get(1).equals("hb") && JSONArray(socketText.text).getJSONArray(1).length()>3 &&
                    JSONArray(socketText.text).getJSONArray(1).length()<11
        }.map { data ->
            data.text?.toTickerModelJsonArray()?.toTickerModel()
        }
        return tickerResult
    }

    override fun subscribeToBook(requestObjBook: JSONObject): Flow<BookModel?> {
        webClient.startSocket(requestObjBook)
        val bookResult = webClient.socketOutput.filter { socketText->
            socketText.text?.startsWith("{") == false && !JSONArray(socketText.text).get(1).equals("hb") && JSONArray(socketText.text).getJSONArray(1).length()<4
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


}


