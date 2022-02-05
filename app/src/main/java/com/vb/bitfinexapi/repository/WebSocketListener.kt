package com.vb.bitfinexapi

import android.util.Log
import com.google.gson.Gson
import com.vb.bitfinexapi.model.InitialServerResponseModel
import com.vb.bitfinexapi.model.ServerHBModel
import com.vb.bitfinexapi.model.SocketData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONException


class WebSocketListener(requestObj: String): WebSocketListener() {

    val socketOutput = MutableSharedFlow<SocketData>()
    val scope = CoroutineScope(Dispatchers.IO)
    var request = requestObj
    var serverStatus = 0
    var jsonStringDefault = "[144436,[41493,9.883866339999997,41495,16.79622643,885,0.0218,41494,3186.63706814,41956,40414]]"

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
        printLog(jsonString)

        if (serverStatus == 0) {
            val serverStatusResultJson = Gson().fromJson(jsonString, InitialServerResponseModel::class.java)
            serverStatus = serverStatusResultJson.platform.status
            printLog(serverStatusResultJson.platform.status.toString())
        }
        if (serverStatus == 1) {
            if (JSONArray(jsonString).getJSONArray(0).getString(2) == "hb") {
            } else {
                jsonStringDefault = jsonString
                scope.launch {
                    socketOutput.emit(SocketData(text = jsonStringDefault))
                }
            }
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

