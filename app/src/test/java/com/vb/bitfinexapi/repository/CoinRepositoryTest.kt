package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.model.networkModel.SocketData
import org.json.JSONArray
import com.google.common.truth.Truth.assertThat
import com.vb.bitfinexapi.model.domainModel.TickerModel
import com.vb.bitfinexapi.model.domainModel.toBookModel
import com.vb.bitfinexapi.model.domainModel.toTickerModel
import io.mockk.mockk
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.json.JSONObject
import org.json.JSONTokener

import org.junit.Test

class CoinRepositoryTest {
    var webclient = mockk<WebClient>()

//    @MockK
//    private var webclient = WebClient()

//    @Before
//    fun setup() {
//        var webclient = mockk<WebClient>()
//    }


    @Test
    fun `subscribeToTicker() properly filters the data`() {
        var localFlowResult: Any? = null

        val string1 = "[374259,[43103,14.551372659999997,43105,7.828219170000001,-858,-0.0195,43100,7844.81332585,45899,42672]]"
        val string2 = "[243861,[42000,10,0.70782275]]"

        val result1 = string1.toTickerModelJsonArray().toTickerModel()
        val socketData1 = SocketData(string1, null, null)
        val sockedData2 = SocketData(string2, null, null)
        val flowText = flowOf(socketData1, sockedData2)

        val flowResult = flowText.filter { socketText ->
            socketText.text?.startsWith("{") == false && !JSONArray(socketText.text).get(1)
                .equals("hb") && JSONArray(socketText.text).getJSONArray(1).length() > 3 &&
                    JSONArray(socketText.text).getJSONArray(1).length() < 11
        }.map { data ->
            data.text?.toTickerModelJsonArray()?.toTickerModel()
        }
        runBlockingTest {
           localFlowResult = flowResult.first()
        }
        assertThat(localFlowResult).isEqualTo(result1)
    }

    @Test
    fun `subscribeToBook() properly filters book data`() {
        var localFlowResult: Any? = null

        val string1 = "[374259,[43103,14.551372659999997,43105,7.828219170000001,-858,-0.0195,43100,7844.81332585,45899,42672]]"
        val string2 = "[243861,[42000,10,0.70782275]]"
        val result1 = string2.toBookModelJsonArray()?.toBookModel()

        val socketData1 = SocketData(string1, null, null)
        val sockedData2 = SocketData(string2, null, null)
        val flowText = flowOf(socketData1, sockedData2)

        val flowResult = flowText.filter { socketText->
            socketText.text?.startsWith("{") == false && !JSONArray(socketText.text).get(1).equals("hb") && JSONArray(socketText.text).getJSONArray(1).length()<4
        }.map { data ->
            data.text?.toBookModelJsonArray()?.toBookModel()
        }
        runBlockingTest {
            localFlowResult = flowResult.last()
        }
        assertThat(localFlowResult).isEqualTo(result1)
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
                        val valueString: String = jsonTicker.get(i).toString()
                        jsonArrayList.add(valueString)
                    }
                }
            }
        }
        return jsonArrayList
    }

fun String.toBookModelJsonArray(): ArrayList<String>? {
    var json = JSONTokener(this).nextValue()
    val jsonArrayList: ArrayList<String> = ArrayList()
    var jsonString = this
    when (json) {
        is JSONObject -> {}  // HANDLE OTHER RESPONSES
        is JSONArray -> {
            var bookTicker = JSONArray(jsonString).getJSONArray(1)
            if (bookTicker.length() > 9) {
                bookTicker = bookTicker.getJSONArray(1)
            }
            for (i in 0 until bookTicker.length()) {
                val valueString: String = bookTicker.get(i).toString()
                jsonArrayList.add(valueString)
            }
        }
        else -> {}
    }
    return jsonArrayList
}

