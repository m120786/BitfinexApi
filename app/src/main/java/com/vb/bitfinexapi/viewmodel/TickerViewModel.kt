package com.vb.bitfinexapi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.bitfinexapi.SocketData
import com.vb.bitfinexapi.model.TickerModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TickerViewModel(): ViewModel()  {

    val tickerData = MutableSharedFlow<SocketData>()

    init {
        collectTickerData()
    }

    private fun collectTickerData() {
        viewModelScope.launch {

        }
    }
}