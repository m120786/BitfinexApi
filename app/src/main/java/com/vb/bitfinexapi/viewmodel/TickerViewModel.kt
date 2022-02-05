package com.vb.bitfinexapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.bitfinexapi.SocketData
import com.vb.bitfinexapi.repository.MainRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class TickerViewModel(val repository: MainRepository): ViewModel()  {
    var tickerData = MutableSharedFlow<SocketData>()

    init {
        collectTickerData()
    }

    private fun collectTickerData() {
        viewModelScope.launch {
            tickerData = repository.subscribeToTicker()
            }
        }
    }
