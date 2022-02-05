package com.vb.bitfinexapi.model

data class TickerModel(
    val channelID: String,
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
    channelID = this[0],
    bid = this[1],
    bidSize = this[2],
    ask = this[3],
    askSize = this[4],
    dailyChange = this[5],
    dailyChangeRelative = this[6],
    lastPrice = this[7],
    volume = this[8],
    high = this[9],
    low =  this[10],

)