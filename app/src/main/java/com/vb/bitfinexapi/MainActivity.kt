package com.vb.bitfinexapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.vb.bitfinexapi.repository.CoinRepository
import com.vb.bitfinexapi.repository.WebClient
import com.vb.bitfinexapi.ui.theme.BitfinexApiTheme
import com.vb.bitfinexapi.utils.Constants
import com.vb.bitfinexapi.viewmodel.TickerViewModel
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestObjBook = JSONObject()
        requestObjBook.put("event", Constants.BOOK_SUBSCRIBE)
        requestObjBook.put("channel", Constants.BOOK_CHANNEL)
        requestObjBook.put("pair", Constants.BOOK_PAIR)
        requestObjBook.put("prec", Constants.BOOK_PRECISION)
        WebClient().startSocket(requestObjBook)




        val repository = CoinRepository()
        val tickerViewModel = TickerViewModel(coinService = repository)
        
        setContent {
            BitfinexApiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CoinListView(tickerViewModel = tickerViewModel)
                }
            }
        }
    }
}

