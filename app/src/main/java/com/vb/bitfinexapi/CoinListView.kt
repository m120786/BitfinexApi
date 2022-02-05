package com.vb.bitfinexapi

import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.vb.bitfinexapi.repository.MainRepository
import com.vb.bitfinexapi.viewmodel.TickerViewModel


@Composable
fun CoinListView(tickerViewModel: TickerViewModel) {


    val data = tickerViewModel.tickerData.collectAsState(SocketData("", byteString = null, null))


    OutlinedButton(onClick = {  }) {
        Text("Get Data")
    }
    data.value.text?.let { Text(it) }
}