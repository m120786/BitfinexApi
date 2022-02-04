package com.vb.bitfinexapi.model

data class SubscribeTickerRequest(
    val event: String,
    val channel: String,
    val symbol: String
)
