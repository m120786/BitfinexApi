package com.vb.bitfinexapi.model

data class TickerModel(
    val bid: String,
    val bidSize: String,
    val ask: String,
    val askSize: String,
    val dailyChange: String,
    val dailyChangeRelative: String,
    val lastPrice: String,
    val volume: String,
    val high: String,
    val low: String
)

fun ArrayList<String>.toTickerModel() = TickerModel(
    bid = this[0],
    bidSize = this[1],
    ask = this[2],
    askSize = this[3],
    dailyChange = this[4],
    dailyChangeRelative = this[5],
    lastPrice = this[6],
    volume = this[7],
    high = this[8],
    low =  this[9]
)