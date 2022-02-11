package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.model.domainModel.toBookModelJson
import com.vb.bitfinexapi.model.networkModel.SocketData
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import org.json.JSONArray
import com.google.common.truth.Truth.assertThat
import com.vb.bitfinexapi.model.domainModel.toTickerModel
import io.mockk.mockk
import io.mockk.mockkClass
import org.json.JSONObject
import org.json.JSONTokener

import org.junit.Test

class CoinRepositoryTest {
//    @MockK
//    private var webclient = WebClient()

    var webclient = mockk<WebClient>()


    @Test
    fun subscribeToTicker() {
        val tickerString = "[374259,[43103,14.551372659999997,43105,7.828219170000001,-858,-0.0195,43100,7844.81332585,45899,42672]]"
        val tickerString2 = "[43103,14.551372659999997,43105,7.828219170000001,-858,-0.0195,43100,7844.81332585,45899,42672]"

        val socketData = SocketData(tickerString, null, null)
        coEvery { webclient.socketOutput.take(1) } returns flowOf(socketData)
        webclient.socketOutput.take(1).filter {  socketText ->
            socketText.text?.startsWith("{") == false && !JSONArray(socketText.text).get(1).equals("hb") && JSONArray(socketText.text).getJSONArray(1).length()>3 &&
                    JSONArray(socketText.text).getJSONArray(1).length()<11
        }.map { data ->
            assertThat(tickerString2.toTickerModelJsonArray().toBookModelJson()).isEqualTo(data.text?.toTickerModelJsonArray()?.toTickerModel())

        }

    }
    fun String.toTickerModelJsonArray(): ArrayList<String> {
        var json = JSONTokener(this).nextValue()
        val jsonArrayList: ArrayList<String> = ArrayList()
        var jsonString = this
        when (json) {
            is JSONObject -> {}
            is JSONArray -> {
                var jsonTicker = JSONArray(jsonString).getJSONArray(1)
                if (jsonTicker.length() > 3 && jsonTicker.length() < 11) {
                    for (i in 0 until jsonTicker.length()) {
                        val valueString: String = jsonTicker.getString(i)
                        jsonArrayList.add(valueString)
                    }
                }
            }
        }
        return jsonArrayList
    }
}