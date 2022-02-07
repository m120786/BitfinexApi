package com.vb.bitfinexapi

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vb.bitfinexapi.model.BookModel
import com.vb.bitfinexapi.model.TickerModel
import com.vb.bitfinexapi.utils.Constants
import com.vb.bitfinexapi.viewmodel.TickerViewModel
import kotlinx.coroutines.flow.collect


@Composable
fun CoinListView(tickerViewModel: TickerViewModel) {
    var launch = 1
    var list = remember { mutableStateListOf<BookModel>() }
//    val list2 = mutableListOf<BookModelJson>()
    var list2 = remember { mutableStateListOf<BookModel>() }
    var i = 0

    val tickerData = tickerViewModel.tickerData.collectAsState(
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
    LaunchedEffect(key1 = launch, block = {
        tickerViewModel.bookData.collect { bookList ->
            list.addAll(bookList)
            Log.v("list", i.toString())
        }
    })


    Column() {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Last Price : ${tickerData.value.lastPrice}")
            Text("Volume : ${tickerData.value.volume}")
            Text("Low : ${tickerData.value.low}")
            Text("High : ${tickerData.value.high}")
            Text("Daily Change : ${tickerData.value.dailyChange}")
        }
        list.takeLast(Constants.BOOK_SIZE).forEach {
            Column() {
                Row() {
                    Text("${it.amount}", modifier = Modifier.padding(5.dp))
                    Text("${it.count}", modifier = Modifier.padding(5.dp))
                    Text("${it.price}", modifier = Modifier.padding(5.dp))
                }
            }
        }
//    Text(list.takeLast(Constants.BOOK_SIZE).toString())

    }
}

