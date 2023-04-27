package com.example.stocks.presentation.state

import com.example.stocks.domain.model.Stock

/**
 * Stocks state used by views
 */
data class StocksState(
    val stocks: List<Stock> = emptyList(),
    val watchList: List<Stock> = emptyList(),
    val stocksViewState: StocksViewState = StocksViewState.LOADING
) {
    val shouldShowStockList = stocks.isNotEmpty() || watchList.isNotEmpty()
}