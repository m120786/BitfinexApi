package com.vb.bitfinexapi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.bitfinexapi.model.BookModel
import com.vb.bitfinexapi.model.TickerModel
import com.vb.bitfinexapi.model.toBookModel
import com.vb.bitfinexapi.model.toTickerModel
import com.vb.bitfinexapi.repository.MainRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener


class TickerViewModel(val repository: MainRepository) : ViewModel() {
    var tickerData = MutableSharedFlow<TickerModel>()
    var bookData = MutableSharedFlow<BookModel>()

    init {
        collectTickerData()
        collectBookData()
    }

    private fun collectBookData() {
        viewModelScope.launch {
            val socketBook = repository.subscribeToBook()
            socketBook.socketOutput.collect { book ->
                val bookJsonString: String? = book.text
                var bookJson = JSONTokener(bookJsonString).nextValue()
                val bookListData: ArrayList<String> = ArrayList()

                when (bookJson) {
                    is JSONObject -> { // HANDLE OTHER RESPONSES
                    }
                    is JSONArray -> {
                        if (JSONArray(bookJsonString).get(1).equals("hb")) {
                        } else {
                            var bookTicker = JSONArray(bookJsonString).getJSONArray(1)
                            for (i in 0 until bookTicker.length()) {
                                val valueString: String = bookTicker.getString(i)
                                bookListData?.add(valueString)
                            }
                            bookData.emit(bookListData!!.toBookModel())
                            Log.v("VM", bookListData.toString())
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    fun collectTickerData() {
        viewModelScope.launch {
            val socketTicker = repository.subscribeToTicker()
            socketTicker.socketOutput.collect { tickerResponse ->
                val tickerJsonString: String? = tickerResponse.text
                var json = JSONTokener(tickerJsonString).nextValue()
                val TickerList: ArrayList<String> = ArrayList()

                when (json) {
                    is JSONObject -> { // HANDLE WHEN SERVER IS ON MAINTENANCE
                    }
                    is JSONArray -> {
                        if (JSONArray(tickerJsonString).get(1).equals("hb")) { } else {
                            var jsonTicker = JSONArray(tickerJsonString).getJSONArray(1)
                            for (i in 0 until jsonTicker.length()) {
                                val valueString: String = jsonTicker.getString(i)
                                TickerList?.add(valueString)
                            }
                            tickerData.emit(TickerList!!.toTickerModel())
                            Log.v("VM", TickerList.toString())
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }
}

