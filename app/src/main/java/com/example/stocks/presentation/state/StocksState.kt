package com.example.stocks.presentation.state

import com.example.stocks.domain.model.Stock

data class StocksState(
    val stocks: List<Stock> = emptyList(),
    val watchList: List<Stock> = emptyList(),
    val viewState: ViewState = ViewState.SUCCESS
) {
    val shouldShowStockList = stocks.isNotEmpty() || watchList.isNotEmpty()
}

enum class ViewState {
    SUCCESS, LOADING, ERROR
}