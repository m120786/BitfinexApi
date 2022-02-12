package com.vb.bitfinexapi

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import com.vb.bitfinexapi.connection.ConnectionState
import com.vb.bitfinexapi.connection.NetworkConnection
import com.vb.bitfinexapi.repository.CoinRepository
import com.vb.bitfinexapi.repository.WebClient
import com.vb.bitfinexapi.ui.theme.BitfinexApiTheme
import com.vb.bitfinexapi.viewmodel.TickerViewModel
import com.vb.bitfinexapi.viewmodel.ViewModelFactory


class MainActivity : ComponentActivity() {
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state = internetConnection.connectionState.collectAsState()
                    when (state.value) {
                        ConnectionState.Available -> {
                            observeData()
                            Log.i("Main", "send update available")
                        }
                        ConnectionState.Unavailable -> closeSocket()
                        ConnectionState.Reconnecting -> {
                            closeSocket()
                            observeData()
                            Log.i("Main", "send update reconnecting")

                        }
                    }


//                    AndroidView(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .wrapContentHeight(),
//                        factory = {
//                            FragmentContainerView(context).apply {
//                                this.getFragment<>()
//                            }
//                        },


                    CoinListView(tickerViewModel = tickerViewModel)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i("Main", "on Pause called")
        webClient.stopSocket()

    }

    override fun onResume() {
        super.onResume()
        Log.i("Main", "on Resume called")
        observeData()

    }


    private fun observeData() {
        tickerViewModel.collectBookData()
        tickerViewModel.collectTickerData()

    }

    private fun closeSocket() {
        webClient.stopSocket()
    }
}

