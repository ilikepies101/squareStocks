package com.example.stocks.domain.repository

import com.example.stocks.domain.model.Stock
import com.example.stocks.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth used when fetching stocks
 */
interface StockRepository {
    fun getStocks() : Flow<Resource<List<Stock>>>
}