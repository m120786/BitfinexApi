package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.model.domainModel.BookModel
import com.vb.bitfinexapi.model.domainModel.TickerModel

class FakeWebClient {
    val books = listOf(
        BookModel(44200, 44, -69.70442136),
        BookModel(44100, 130, -83.04659358),
        BookModel(43800, 130, 61.35840244),
        BookModel(45100, 40, -8.22319656),
        BookModel(44900, 31, -30.74594966),
        BookModel(44600, 19, -26.82164771),
        BookModel(44400, 26, -65.40188566),
        BookModel(44300, 26, -51.00348899),
        BookModel(44100,128,-125.0750562),
        BookModel(43500,71,44.30826007)
    )
    val ticker = listOf(
        TickerModel("43970", bidSize="10.96605438", ask="43976", askSize="14.032752639999996", dailyChange="-706.55298819", dailyChangeRelative="-0.0158", lastPrice="43974.1684764", volume="6826.71395314", high="45899", low="43222")
    )


}