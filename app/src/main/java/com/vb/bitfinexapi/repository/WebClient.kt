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

            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                socketOutput.tryEmit(SocketData(text = text))
                Log.i("webClient", text)


            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)

            }
        }
    private val socketHttpClient = OkHttpClient.Builder()
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

    private val request = Request.Builder()
            .url("wss://api-pub.bitfinex.com/ws/2")
            .build()

    var webSocketLocal = socketHttpClient.newWebSocket(request, webSocketListener2)


    fun startSocket(requestObj: JSONObject) {
            webSocketLocal?.send(requestObj.toString())
              Log.i("start", webSocketLocal.toString())

    }

    fun stopSocket() {
        webSocketLocal?.close(1000, "close")
    }

 }

//        fun sendMessage() {
//            webSocketListener2.onMessage()
//            // atsidaryti, laukti, kaupti kažkur message'us, kaupti kažkur duomenis kai atsidarys, kaupti duomenis list'e
//        }


