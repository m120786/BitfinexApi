package com.vb.bitfinexapi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.bitfinexapi.model.BookModel
import com.vb.bitfinexapi.model.TickerModel
import com.vb.bitfinexapi.repository.CoinRepository
import com.vb.bitfinexapi.repository.CoinService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList


class TickerViewModel(val coinService: CoinService) : ViewModel() {
    var tickerData = MutableStateFlow(TickerModel("0", "0","0","0","0","0","0","0","0","0"))
    var bookData = MutableStateFlow<List<BookModel>>(emptyList())
    var list = mutableListOf<BookModel>()

    fun collectBookData() {
        viewModelScope.launch {
            coinService.subscribeToBook().collect { book ->
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
            coinService.subscribeToTicker().collect { ticker ->
                if (ticker != null) {
                    tickerData.value = ticker!!
                    Log.i("viewModel", ticker.toString())
                }
            }

            }
        }
    }

