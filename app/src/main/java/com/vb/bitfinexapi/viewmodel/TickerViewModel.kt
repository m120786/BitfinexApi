package com.vb.bitfinexapi.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.bitfinexapi.connection.NetworkConnection
import com.vb.bitfinexapi.model.domainModel.BookModel
import com.vb.bitfinexapi.model.domainModel.TickerModel
import com.vb.bitfinexapi.repository.CoinService
import com.vb.bitfinexapi.viewmodel.ViewModelConstants.BOOK_CHANNEL
import com.vb.bitfinexapi.viewmodel.ViewModelConstants.BOOK_PAIR
import com.vb.bitfinexapi.viewmodel.ViewModelConstants.BOOK_PRECISION
import com.vb.bitfinexapi.viewmodel.ViewModelConstants.BOOK_SUBSCRIBE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import org.json.JSONObject


class TickerViewModel(val coinService: CoinService) : ViewModel() {
    var tickerData = MutableStateFlow(TickerModel("0", "0", "0", "0", "0", "0", "0", "0", "0", "0"))
    var bookData = MutableStateFlow<List<BookModel>>(emptyList())
    var errorData = MutableStateFlow<Boolean>(false)
    private var list = mutableListOf<BookModel>()



    fun collectBookData() {
        val requestObjBook = JSONObject()
        requestObjBook.put("event", BOOK_SUBSCRIBE)
        requestObjBook.put("channel", BOOK_CHANNEL)
        requestObjBook.put("pair", BOOK_PAIR)
        requestObjBook.put("prec", BOOK_PRECISION)
        viewModelScope.launch {
            coinService.subscribeToBook(requestObjBook).collect { book ->
                if (book != null) {
                    list.add(book)
                }
                if (list.size > 9) {
                    bookData.value = list.toImmutableList().reversed()
                    Log.i("book", list.toString())
                    list.clear()
                }

            }
        }
    }

    fun collectTickerData() {
        val requestObjTicker = JSONObject()
        requestObjTicker.put("event", ViewModelConstants.TICKER_SUBSCRIBE)
        requestObjTicker.put("channel", ViewModelConstants.TICKER_CHANNEL)
        requestObjTicker.put("symbol", ViewModelConstants.TICKER_SYMBOL)

        viewModelScope.launch {
            coinService.subscribeToTicker(requestObjTicker).collect { ticker ->
                if (ticker != null) {
                    tickerData.value = ticker
                }
                Log.i("view_model", ticker.toString())
            }
        }
    }

    fun collectError() {
        viewModelScope.launch {
            coinService.subscribeToError().collect { error ->
                errorData.value = error != null
            }
        }
    }
}

