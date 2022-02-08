package com.vb.bitfinexapi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vb.bitfinexapi.viewmodel.TickerViewModel


@Composable
fun CoinListView(tickerViewModel: TickerViewModel) {

    val tickerData = tickerViewModel.tickerData.collectAsState()

    val bookData = tickerViewModel.bookData.collectAsState()


    Column() {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Last Price : ${tickerData.value.lastPrice}")
            Text("Volume : ${tickerData.value.volume}")
            Text("Low : ${tickerData.value.low}")
            Text("High : ${tickerData.value.high}")
            Text("Daily Change : ${tickerData.value.dailyChange}")
        }
        bookData.value.forEach {
            Column() {
                Row() {
                    Text("${it.amount}", modifier = Modifier.padding(5.dp))
                    Text("${it.count}", modifier = Modifier.padding(5.dp))
                    Text("${it.price}", modifier = Modifier.padding(5.dp))
                }
            }
        }
    }
}

