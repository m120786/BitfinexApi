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
    var tickerData = MutableStateFlow(TickerModel("0", "0","0","0","0","0","0","0","0","0"))
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
//                    Log.i("viewModel", list.toString())
                    list.clear()
                }

            }
        }
    }

    fun collectTickerData() {
        viewModelScope.launch {
            repository.subscribeToTicker().collect { ticker ->
                tickerData.value = ticker!!
                Log.i("viewModel", ticker.toString())

            }

            }
        }
    }

