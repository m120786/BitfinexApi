package com.vb.bitfinexapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.Gson
import com.vb.bitfinexapi.model.SubscribeTickerRequest
import com.vb.bitfinexapi.ui.theme.BitfinexApiTheme
import com.vb.bitfinexapi.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gson = Gson()
        var jsonString = gson.toJson(SubscribeTickerRequest(Constants.TICKER_EVENT, Constants.TICKER_CHANNEL, Constants.TICKER_SYMBOL))


        val socketHttpClient = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("wss://api-pub.bitfinex.com/ws/2")
            .build()

        val webSocketListener = WebSocketListener()
        val webSocket = socketHttpClient.newWebSocket(request, webSocketListener)



        setContent {
            BitfinexApiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background

                ) {
                    OutlinedButton(onClick = {  }) {
                        Text("Get Data")

                    }
                }
            }
        }
    }
}

