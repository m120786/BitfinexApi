package com.vb.bitfinexapi

import android.util.Log
import com.vb.bitfinexapi.model.networkModel.SocketData
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class WebSocketListener(val request: String) : WebSocketListener() {

    val socketOutput = MutableSharedFlow<SocketData>(replay = 1)

    override fun onOpen(webSocket: WebSocket, response: Response) {
        printLog(webSocket.toString())
        webSocket.send(request)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        printLog("Closed")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
            socketOutput.tryEmit(SocketData(exception = Exception()))
    }

    override fun onMessage(webSocket: WebSocket, jsonString: String) {
            socketOutput.tryEmit(SocketData(text = jsonString))
            printLog(jsonString)
    }


    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        printLog("Error" + t.message)
            socketOutput.tryEmit(SocketData(exception = t))
    }

}

private fun printLog(txt: String) {
    Log.v("WSS", txt)
}



