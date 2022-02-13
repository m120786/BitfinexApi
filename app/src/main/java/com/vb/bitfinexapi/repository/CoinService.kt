package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.model.domainModel.BookModel
import com.vb.bitfinexapi.model.domainModel.TickerModel
import com.vb.bitfinexapi.model.networkModel.SocketData
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

interface CoinService {
    fun subscribeToTicker(requestObjTicker: JSONObject): Flow<TickerModel?>
    fun subscribeToBook(requestObjBook: JSONObject):  Flow<BookModel?>
    fun unsubscribeTicker()
    fun unsubscribeBook()
    fun subscribeToError(): Flow<Throwable?>
}