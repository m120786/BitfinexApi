package com.vb.bitfinexapi

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.vb.bitfinexapi.model.SocketData
import com.vb.bitfinexapi.model.toTickerModel
import com.vb.bitfinexapi.viewmodel.TickerViewModel
import org.json.JSONArray
import org.json.JSONException


@Composable
fun CoinListView(tickerViewModel: TickerViewModel) {


    val data = tickerViewModel.tickerData.collectAsState("[101819,[41815,10.94994413,41821,8.13565286,2224,0.0562,41815,4858.64416913,41956,39287]]")

    val listData: ArrayList<String>? = null

    try {
        val jsonArray = JSONArray(data.value).getJSONArray(1)
        for (i in 0 until jsonArray.length()) {
            val valueString: String = jsonArray.getString(i)
            listData?.add(valueString)
            Log.e("json", "$i=$valueString")
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    var tickerData = listData?.toTickerModel()
    Column() {
        Text("Last Price : ${tickerData?.lastPrice}")
        Text("Volume : ${tickerData?.volume}")
        Text("Low : ${tickerData?.low}")
        Text("High : ${tickerData?.high}")
        Text("Daily Change : ${tickerData?.dailyChange}")
    }
}