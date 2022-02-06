package com.vb.bitfinexapi

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.vb.bitfinexapi.model.TickerModel
import com.vb.bitfinexapi.viewmodel.TickerViewModel


@Composable
fun CoinListView(tickerViewModel: TickerViewModel) {


    val data = tickerViewModel.tickerData.collectAsState(
        TickerModel(
            bid = "0",
            bidSize = "0",
            ask = "0",
            askSize = "0",
            dailyChange = "0",
            dailyChangeRelative = "0",
            lastPrice = "0",
            volume = "0",
            high = "0",
            low = "0"
        )
    )

    var tickerData = data.value
    Column() {
        Text("Last Price : ${tickerData?.lastPrice}")
        Text("Volume : ${tickerData?.volume}")
        Text("Low : ${tickerData?.low}")
        Text("High : ${tickerData?.high}")
        Text("Daily Change : ${tickerData?.dailyChange}")
    }
}