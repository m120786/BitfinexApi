package com.vb.bitfinexapi.model.networkModel

import okio.ByteString

data class SocketData(
    val text: String? = null,
    val byteString: ByteString? = null,
    val exception: Throwable? = null
)
