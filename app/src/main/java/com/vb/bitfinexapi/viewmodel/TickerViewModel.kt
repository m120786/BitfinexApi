package com.vb.bitfinexapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.bitfinexapi.model.TickerModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TickerViewModel(): ViewModel()  {

    val tickerData = MutableStateFlow((TickerModel("0","0","0","0","0","0","0","0","0","0","0")))

    init {
        collectTickerData()
    }

    private fun collectTickerData() {
        viewModelScope.launch {

        }
    }
}