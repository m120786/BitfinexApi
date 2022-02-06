package com.vb.bitfinexapi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.bitfinexapi.repository.MainRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener


class TickerViewModel(val repository: MainRepository) : ViewModel() {
    var tickerData = MutableSharedFlow<String>()

    init {
        collectTickerData()
    }

    fun collectTickerData() {
        viewModelScope.launch {
            val socketTicker = repository.subscribeToTicker()
            socketTicker.socketOutput.collectLatest { data ->
                val jsonString: String? = data.text

                var json = JSONTokener(jsonString).nextValue()
                when (json) {
                    is JSONObject -> { //it is a JsonObject
                    }
                    is JSONArray -> {
                        if (JSONArray(jsonString).get(1).equals("hb")) {
                        } else {
                            tickerData.emit(jsonString!!)
                            Log.v("VM", jsonString)
                        }
                    }
                    else -> { //handle the odd scenario
                    }
                }
            }
        }
    }
}

