package com.vb.bitfinexapi.model

import okio.ByteString

data class SocketData(
    val text: String? = null,
    val byteString: ByteString? = null,
    val exception: Throwable? = null
)