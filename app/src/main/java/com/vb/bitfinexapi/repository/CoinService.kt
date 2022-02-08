package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.model.domainModel.BookModel
import com.vb.bitfinexapi.model.domainModel.TickerModel
import kotlinx.coroutines.flow.Flow

interface CoinService {
    fun subscribeToTicker(): Flow<TickerModel?>
    fun subscribeToBook():  Flow<BookModel?>
    fun unsubscribeTicker()
    fun unsubscribeBook()
}