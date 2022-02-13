package com.vb.bitfinexapi

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vb.bitfinexapi.connection.ConnectionState
import com.vb.bitfinexapi.connection.NetworkConnection
import com.vb.bitfinexapi.repository.CoinRepository
import com.vb.bitfinexapi.repository.WebClient
import com.vb.bitfinexapi.ui.theme.BitfinexApiTheme
import com.vb.bitfinexapi.viewmodel.TickerViewModel
import com.vb.bitfinexapi.viewmodel.ViewModelFactory


class MainActivity : ComponentActivity() {
    private val TAG_MAIN = "Main"

    private val webClient = WebClient()
    private val repository = CoinRepository(webClient)
    private val tickerViewModel: TickerViewModel by viewModels {
        ViewModelFactory(repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context: Context = baseContext

        val internetConnection = NetworkConnection(context)
        internetConnection.registerForConnectionState()                 //registers for network connection state

        setContent {
            BitfinexApiTheme {
                Scaffold(    topBar = {
                    TopAppBar {
                        Row {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_currency_bitcoin_24),
                            contentDescription = "play_icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                            Text("USD / BTC", modifier = Modifier.padding(start = 10.dp))
                        }}
                }) {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state = internetConnection.connectionState.collectAsState()
                    when (state.value) {
                        ConnectionState.Available -> {
                            observeData()
                            Log.i(TAG_MAIN, "send update available")
                        }
                        ConnectionState.Unavailable -> closeSocket()
                        ConnectionState.Reconnecting -> {
                            closeSocket()
                            observeData()
                            Log.i(TAG_MAIN, "send update reconnecting")

                        }
                    }
                    CoinListView(tickerViewModel = tickerViewModel)
                }
            }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG_MAIN, "on Pause called")
        webClient.stopSocket()

    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG_MAIN, "on Resume called")
        observeData()

    }


    private fun observeData() {
        tickerViewModel.collectBookData()
        tickerViewModel.collectTickerData()
        tickerViewModel.collectError()

    }

    private fun closeSocket() {
        webClient.stopSocket()
    }
}

