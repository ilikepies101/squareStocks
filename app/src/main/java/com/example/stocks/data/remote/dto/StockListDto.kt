package com.example.stocks.data.remote.dto

import com.example.stocks.data.local.entity.StockEntity
import com.example.stocks.domain.model.Stock

data class StockListDto(
    val stocks: List<StockDto>
)

