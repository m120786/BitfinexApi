package com.vb.bitfinexapi.model

data class BookModelJson(
    val price: String,
    val count: String,
    val amount: String,
)

fun ArrayList<String>.toBookModel() = BookModelJson(
    price = this[0],
    count = this[1],
    amount = this[2]
)

fun BookModelJson.toBookModel(): BookModel {
    return BookModel(
        price = price.toInt(),
        count = count.toInt(),
        amount = amount.toDouble()
    )

//    fun ArrayList<String>.toBookModel(): BookModel{
//        BookModel
//        price = this[0],
//        count = this[1],
//        amount = this[2]
//    )


}