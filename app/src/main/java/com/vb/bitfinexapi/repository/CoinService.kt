package com.vb.bitfinexapi.repository

import com.vb.bitfinexapi.model.BookModel
import com.vb.bitfinexapi.model.TickerModel
import kotlinx.coroutines.flow.Flow

interface CoinService {
    fun subscribeToTicker(): Flow<TickerModel?>
    fun subscribeToBook():  Flow<BookModel?>
}