package com.vb.bitfinexapi

import android.util.Log
import com.vb.bitfinexapi.model.SocketData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class WebSocketListener(requestObj: String) : WebSocketListener() {

    val socketOutput = MutableSharedFlow<SocketData>()
    val scope = CoroutineScope(Dispatchers.IO)
    var request = requestObj

    override fun onOpen(webSocket: WebSocket, response: Response) {
        webSocket.send(request)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        printLog("Closed")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        scope.launch {
            socketOutput.emit(SocketData(exception = Exception()))
        }
    }

    override fun onMessage(webSocket: WebSocket, jsonString: String) {
        scope.launch {
            socketOutput.emit(SocketData(text = jsonString))
            printLog(jsonString)
        }

    }


    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        printLog("Error" + t.message)
        scope.launch {
            socketOutput.tryEmit(SocketData(exception = t))
        }
    }

}

private fun printLog(txt: String) {
    Log.v("WSS", txt)
}

