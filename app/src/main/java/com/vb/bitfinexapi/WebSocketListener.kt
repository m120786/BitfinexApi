package com.vb.bitfinexapi

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class WebSocketListener: WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        val requestObj= JSONObject()
        requestObj.put("event","subscribe")
        requestObj.put("channel","ticker")
        requestObj.put("symbol","tBTCUSD")
        webSocket.send(requestObj.toString())
//        webSocket.close(1000, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        printLog("Closed")

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        printLog("closing")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        printLog(text)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        printLog("Error" + t.message)
    }

}

private fun printLog(txt: String) {
    Log.v("WSS", txt)
}

data class SubscribeTicker(
    val event: String,
    val channel: String,
    val symbol: String
)