package com.vb.bitfinexapi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import com.vb.bitfinexapi.repository.CoinRepository
import com.vb.bitfinexapi.repository.WebClient
import com.vb.bitfinexapi.ui.theme.BitfinexApiTheme
import com.vb.bitfinexapi.utils.Constants
import com.vb.bitfinexapi.viewmodel.TickerViewModel
import com.vb.bitfinexapi.viewmodel.ViewModelFactory
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    val webClient = WebClient()
    val repository = CoinRepository(webClient)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tickerViewModel: TickerViewModel by viewModels {
            ViewModelFactory(repository)
        }
        tickerViewModel.collectBookData()
        tickerViewModel.collectTickerData()

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

    override fun onPause() {
        super.onPause()



        Log.i("Main", "on Pause called")

//        val requestObjTicker = JSONObject()
//        requestObjTicker.put("event", "unsubscribe")
//        requestObjTicker.put("channel", Constants.TICKER_CHANNEL)
//
//        val requestObjBook = JSONObject()
//        requestObjBook.put("event", "unsubscribe")
//        requestObjBook.put("channel", Constants.BOOK_CHANNEL)
        webClient.stopSocket()

    }
}

