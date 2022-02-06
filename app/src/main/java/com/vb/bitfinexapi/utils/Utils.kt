package com.vb.bitfinexapi.utils

import com.vb.bitfinexapi.model.BookModelJson

class Utils {
    fun getOrderedList(list: List<BookModelJson>): List<BookModelJson> {
        return list.filter { it.amount.toInt() > 0 }.reversed().take(10)
    }
}