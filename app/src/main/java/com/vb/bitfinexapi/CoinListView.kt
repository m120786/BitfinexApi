package com.vb.bitfinexapi

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vb.bitfinexapi.viewmodel.TickerViewModel


@Composable
fun CoinListView(tickerViewModel: TickerViewModel) {

    val tickerData = tickerViewModel.tickerData.collectAsState()
    val context = LocalContext.current
    val bookData = tickerViewModel.bookData.collectAsState()

    val errorData = tickerViewModel.errorData.collectAsState()




    Column {
        if (errorData.value == true) {
            Text("Server Error")
        }
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Last Price : ${tickerData.value.lastPrice}")
            Text("Volume : ${tickerData.value.volume}")
            Text("Low : ${tickerData.value.low}")
            Text("High : ${tickerData.value.high}")
            Text("Daily Change : ${tickerData.value.dailyChange}")
        }
        RowOfIcons()
        bookData.value.forEach {
            Column {
                Row( verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, top = 2.dp)) {
                    val color = if (it.amount < 0) Color.Red else Color.Green
                    Text("${it.price}", modifier = Modifier.padding(5.dp), color = color)
                    Text("${it.count}", modifier = Modifier.padding(5.dp),color = color)
                    Text("${Math.round(it.amount * 1000.0) / 1000.0}", modifier = Modifier.padding(5.dp), color = color)
                }
            }
        }
    }
}
@Composable
fun RowOfIcons() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_currency_exchange_24),
            contentDescription = "play_icon",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_numbers_24),
            contentDescription = "stop_icon",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_up_down),
            contentDescription = "time_icon",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
    }
}