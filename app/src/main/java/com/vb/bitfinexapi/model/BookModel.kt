package com.vb.bitfinexapi.model

data class BookModel(
    val price: String,
    val count: String,
    val amount: String,
)

fun ArrayList<String>.toBookModel() = BookModel(
    price = this[0],
    count = this[1],
    amount = this[2]
)