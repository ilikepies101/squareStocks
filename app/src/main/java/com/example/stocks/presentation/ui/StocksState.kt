package com.example.stocks.presentation.ui

import com.example.stocks.domain.model.Stock

data class StocksState(
    val stocks: List<Stock> = emptyList(),
    val watchList: List<Stock> = emptyList(),
    val isLoading: Boolean = false
)