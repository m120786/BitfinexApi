package com.vb.bitfinexapi

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.vb.bitfinexapi.model.BookModel
import com.vb.bitfinexapi.model.TickerModel
import com.vb.bitfinexapi.viewmodel.TickerViewModel


@Composable
fun CoinListView(tickerViewModel: TickerViewModel) {
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
    var bookData = tickerViewModel.bookData.collectAsState(initial =
    BookModel(
        price = "0",
        count = "0",
        amount = "0"
    ))

Column() {
    Column() {
        Text("Last Price : ${tickerData?.value.lastPrice}")
        Text("Volume : ${tickerData?.value.volume}")
        Text("Low : ${tickerData?.value.low}")
        Text("High : ${tickerData?.value.high}")
        Text("Daily Change : ${tickerData?.value.dailyChange}")
    }

//    var displayList: ArrayList<BookModel> = ArrayList()

//    val displayList = ArrayList<BookModel>()
    val list =  remember { mutableStateListOf<BookModel>() }
    list.add(bookData.value)
    Log.v("list", list[0].toString())
    if (list.size>1) {      Log.v("list2", list[1].toString()) }


    Text(bookData.value.amount)
    LazyColumn() {
        item {

        }
    }
}}
