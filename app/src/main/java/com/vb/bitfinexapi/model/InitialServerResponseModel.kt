package com.vb.bitfinexapi.model


data class InitialServerResponseModel(
    val event: String,
    val platform: Platform,
    val serverId: String,
    val version: Int
)