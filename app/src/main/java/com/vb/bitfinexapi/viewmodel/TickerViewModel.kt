package com.vb.bitfinexapi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.bitfinexapi.model.TickerModel
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

    init {
        collectTickerData()
        collectBookData()
    }

    private fun collectBookData() {
        viewModelScope.launch {
            val socketBook = repository.subscribeToBook()
            socketBook.socketOutput.collect { bookData ->
                Log.v("Book", bookData.toString())
            }


        }
    }

    fun collectTickerData() {
        viewModelScope.launch {
            val socketTicker = repository.subscribeToTicker()
            socketTicker.socketOutput.collect { data ->
                val jsonString: String? = data.text
                var json = JSONTokener(jsonString).nextValue()
                val listData: ArrayList<String> = ArrayList()

                when (json) {
                    is JSONObject -> { // HANDLE WHEN SERVER IS ON MAINTENANCE
                    }
                    is JSONArray -> {
                        if (JSONArray(jsonString).get(1).equals("hb")) { } else {
                            var jsonTicker = JSONArray(jsonString).getJSONArray(1)
                            for (i in 0 until jsonTicker.length()) {
                                val valueString: String = jsonTicker.getString(i)
                                listData?.add(valueString)
                            }
                            tickerData.emit(listData!!.toTickerModel())
                            Log.v("VM", listData.toString())
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }
}

