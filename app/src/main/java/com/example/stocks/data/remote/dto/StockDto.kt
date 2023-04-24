package com.example.stocks.data.remote.dto

import com.example.stocks.data.local.entity.StockEntity
import com.example.stocks.domain.model.Stock

data class StockDto(
    val currency: String,
    val current_price_cents: Int,
    val current_price_timestamp: Int,
    val name: String,
    val quantity: Int?,
    val ticker: String
) {

    fun toStockEntity() = StockEntity(
        currency = currency,
        currentPriceCents = current_price_cents,
        currentPriceTimestamp = current_price_timestamp,
        name = name,
        quantity = quantity,
        ticker = ticker
    )
}

