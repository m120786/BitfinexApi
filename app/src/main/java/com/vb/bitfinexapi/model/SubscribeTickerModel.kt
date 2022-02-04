package com.vb.bitfinexapi.model

data class SubscribeTickerModel(
    val event: String,
    val channel: String,
    val symbol: String,
)
