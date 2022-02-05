package com.vb.bitfinexapi

import android.util.Log
import com.vb.bitfinexapi.model.TickerModel
import com.vb.bitfinexapi.model.toTickerModel
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONArray
import org.json.JSONException
import java.util.concurrent.Flow

class WebSocketListener(requestObj: String): WebSocketListener() {

    val messageChannel = MutableSharedFlow<TickerModel>()

    var request = requestObj
    override fun onOpen(webSocket: WebSocket, response: Response) {
        webSocket.send(request)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        printLog("Closed")

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        printLog("closing")
    }

    override fun onMessage(webSocket: WebSocket, jsonString: String) {
        printLog(jsonString)

        val listData = ArrayList<String>()
        try {
            val jsonArray = JSONArray(jsonString).getJSONArray(1)
            for (i in 0 until jsonArray.length()) {
                val valueString: String = jsonArray.getString(i)
                listData.add(valueString)
                Log.e("json", "$i=$valueString")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        messageChannel.tryEmit(listData.toTickerModel())
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        printLog("Error" + t.message)
    }

}

private fun printLog(txt: String) {
    Log.v("WSS", txt)
}

data class SocketUpdate(
    val text: String? = null,
    val byteString: ByteString? = null,
    val exception: Throwable? = null
)