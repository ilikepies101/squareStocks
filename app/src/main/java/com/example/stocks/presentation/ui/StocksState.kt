package com.example.stocks.presentation.ui

import com.example.stocks.domain.model.Stock

data class StocksState(
    val stocks: List<Stock> = emptyList(),
    val watchList: List<Stock> = emptyList(),
    val viewState: ViewState = ViewState.SUCCESS
)

enum class ViewState {
    SUCCESS, LOADING, ERROR
}