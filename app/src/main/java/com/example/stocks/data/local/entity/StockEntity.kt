package com.example.stocks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stocks.domain.model.Stock

@Entity
data class StockEntity(
    val currency: String,
    val currentPriceCents: Int,
    val currentPriceTimestamp: Int,
    val name: String,
    val quantity: Int?,
    @PrimaryKey val ticker: String
) {
    fun toStock() = Stock(
        currency = currency,
        currentPriceCents = currentPriceCents,
        currentPriceTimestamp = currentPriceTimestamp,
        name = name,
        quantity = quantity,
        ticker = ticker
    )
}