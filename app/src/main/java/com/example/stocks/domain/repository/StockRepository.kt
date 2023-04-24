package com.example.stocks.domain.repository

import com.example.stocks.domain.model.Stock
import com.example.stocks.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun getStocks() : Flow<Resource<List<Stock>>>
}