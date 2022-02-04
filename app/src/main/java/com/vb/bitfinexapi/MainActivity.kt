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
import com.vb.bitfinexapi.model.SubscribeTickerModel
import com.vb.bitfinexapi.repository.Repository
import com.vb.bitfinexapi.ui.theme.BitfinexApiTheme
import com.vb.bitfinexapi.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = Repository()

//        val requestObj= JSONObject()
//        requestObj.put("event",Constants.TICKER_EVENT)
//        requestObj.put("channel",Constants.TICKER_CHANNEL)
//        requestObj.put("symbol",Constants.TICKER_SYMBOL)

//        val gson = Gson()
//        var jsonString = gson.toJson(SubscribeTickerModel(Constants.TICKER_EVENT, Constants.TICKER_CHANNEL, Constants.TICKER_SYMBOL))






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

