package com.vb.bitfinexapi

import android.util.Log
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.vb.bitfinexapi.model.toTickerModel
import com.vb.bitfinexapi.viewmodel.TickerViewModel
import org.json.JSONArray
import org.json.JSONException


@Composable
fun CoinListView(tickerViewModel: TickerViewModel) {


    val data = tickerViewModel.tickerData.collectAsState(SocketData("[101819,[41815,10.94994413,41821,8.13565286,2224,0.0562,41815,4858.64416913,41956,39287]]", byteString = null, null))


    OutlinedButton(onClick = {  }) {
        Text("Get Data")
    }

    val listData = ArrayList<String>()
    try {
        val jsonArray = JSONArray(data.value.text).getJSONArray(1)
        for (i in 0 until jsonArray.length()) {
            val valueString: String = jsonArray.getString(i)
            listData.add(valueString)
            Log.e("json", "$i=$valueString")
        }
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    var tickerData = listData.toTickerModel()
    Text(tickerData.bidSize)

}