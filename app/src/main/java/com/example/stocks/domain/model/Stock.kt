package com.example.stocks.domain.model
data class Stock (
    val currency: String,
    val currentPriceCents: Int,
    val currentPriceTimestamp: Int,
    val name: String,
    val quantity: Int?,
    val ticker: String
)