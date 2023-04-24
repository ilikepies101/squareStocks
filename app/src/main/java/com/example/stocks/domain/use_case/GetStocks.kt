package com.example.stocks.domain.use_case

import com.example.stocks.domain.repository.StockRepository

class GetStocks(
    private val repository: StockRepository
) {

    operator fun invoke() = repository.getStocks()
}