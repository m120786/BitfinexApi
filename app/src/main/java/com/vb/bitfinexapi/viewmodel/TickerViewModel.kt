package com.vb.bitfinexapi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.bitfinexapi.model.BookModel
import com.vb.bitfinexapi.model.TickerModel
import com.vb.bitfinexapi.model.toTickerModel
import com.vb.bitfinexapi.repository.MainRepository
import com.vb.bitfinexapi.utils.Constants
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener


class TickerViewModel(val repository: MainRepository) : ViewModel() {
    var tickerData = MutableSharedFlow<TickerModel>()
    var bookData = MutableStateFlow<List<BookModel>>(emptyList())
    var list = mutableListOf<BookModel>()
    init {
        collectTickerData()
        collectBookData()
    }

    private fun collectBookData() {
        viewModelScope.launch {
            repository.subscribeToBook().collect { book ->
                if (book != null) {
                    list.add(book)
                }
                if(list.size>9) {
                    bookData.value = list.toImmutableList().reversed()
                    Log.i("viewModel", list.toString())
                    list.clear()
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
                        if (JSONArray(tickerJsonString).get(1).equals("hb")) {
                        } else {
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

