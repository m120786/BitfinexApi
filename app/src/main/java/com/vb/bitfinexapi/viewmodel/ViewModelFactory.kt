package com.vb.bitfinexapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vb.bitfinexapi.repository.CoinService

class ViewModelFactory(private val coinService: CoinService) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TickerViewModel::class.java)) {
            return TickerViewModel(coinService) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}