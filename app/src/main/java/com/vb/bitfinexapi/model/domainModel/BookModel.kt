package com.vb.bitfinexapi.model.domainModel

data class BookModel(
    val price: Int,
    val count: Int,
    val amount: Double
)

fun ArrayList<String>.toBookModel(): BookModel {
    return BookModel(
        price = this[0].toInt(),
        count = this[1].toInt(),
        amount = this[2].toDouble()
    )
}