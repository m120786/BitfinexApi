package com.vb.bitfinexapi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.bitfinexapi.model.domainModel.BookModel
import com.vb.bitfinexapi.model.domainModel.TickerModel
import com.vb.bitfinexapi.repository.CoinRepository
import com.vb.bitfinexapi.repository.CoinService
import com.vb.bitfinexapi.viewmodel.TickerViewModel.Companion.TICKER_CHANNEL
import com.vb.bitfinexapi.viewmodel.TickerViewModel.Companion.TICKER_SUBSCRIBE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import org.json.JSONObject


class TickerViewModel(val coinService: CoinService) : ViewModel() {
    var tickerData = MutableStateFlow(TickerModel("0", "0","0","0","0","0","0","0","0","0"))
    var bookData = MutableStateFlow<List<BookModel>>(emptyList())
    var list = mutableListOf<BookModel>()



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
                if(list.size>9) {
                    bookData.value = list.toImmutableList().reversed()
                    Log.i("viewModel", list.toString())
                    list.clear()
                }

            }
        }
    }

    fun collectTickerData() {
        val requestObjTicker = JSONObject()
        requestObjTicker.put("event", "subscribe")
        requestObjTicker.put("channel", "ticker")
        requestObjTicker.put("symbol", "tBTCUSD")

        viewModelScope.launch {
            coinService.subscribeToTicker(requestObjTicker).collect { ticker ->
                if (ticker != null) {
                    tickerData.value = ticker!!
                    Log.i("viewModel", ticker.toString())
                }
            }

            }
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

