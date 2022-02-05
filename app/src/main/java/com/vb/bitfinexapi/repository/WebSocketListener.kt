package com.vb.bitfinexapi

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONArray
import org.json.JSONException
import java.lang.Exception

class WebSocketListener(requestObj: String): WebSocketListener() {

    val socketOutputChannel = MutableSharedFlow<SocketData>()

    var request = requestObj
    override fun onOpen(webSocket: WebSocket, response: Response) {
        webSocket.send(request)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        printLog("Closed")

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        socketOutputChannel.tryEmit(SocketData(exception = Exception()))
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
        socketOutputChannel.tryEmit(SocketData(text = jsonString))
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        printLog("Error" + t.message)
        socketOutputChannel.tryEmit(SocketData(exception = t))

    }

}

private fun printLog(txt: String) {
    Log.v("WSS", txt)
}

data class SocketData(
    val text: String? = null,
    val byteString: ByteString? = null,
    val exception: Throwable? = null
)