package com.vb.bitfinexapi.repository

import android.util.Log
import com.vb.bitfinexapi.model.networkModel.SocketData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.*
import okio.ByteString
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class WebClient() {

    val socketOutput = MutableSharedFlow<SocketData>(1, extraBufferCapacity = 100, BufferOverflow.DROP_OLDEST)

        private val webSocketListener2: WebSocketListener = object : WebSocketListener() {

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                webSocket.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                socketOutput.tryEmit(SocketData(exception = t))
                Log.i("Main", "OnFailure" + response.toString())
                Log.i("Main", "OnFailure" + t.toString())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                socketOutput.tryEmit(SocketData(text = text))
                Log.i("webClient", text)


            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.i("Main", "Open socket" + response.toString())

            }
        }
    private val socketHttpClient = OkHttpClient.Builder()
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    private val request = Request.Builder()
            .url("wss://api-pub.bitfinex.com/ws/2")
            .build()

        var webSocketLocal: WebSocket? = socketHttpClient.newWebSocket(request, webSocketListener2)

    fun startSocket(requestObj: JSONObject) {
            if (webSocketLocal == null) {
                webSocketLocal = socketHttpClient.newWebSocket(request, webSocketListener2)
                webSocketLocal?.send(requestObj.toString())
                Log.i("Main", webSocketLocal.toString())
            } else {
                webSocketLocal?.send(requestObj.toString())
                Log.i("Main", requestObj.toString())
                Log.i("Main", webSocketLocal.toString())
            }
    }

    fun stopSocket() {
        if (webSocketLocal != null) {
            webSocketLocal?.close(1000, "close")
            webSocketLocal = null
        }
        Log.i("Main", "websocket closed")

    }

 }


