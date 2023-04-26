package com.example.stocks.domain.use_case

import com.example.stocks.domain.repository.StockRepository
import javax.inject.Inject

class GetStocks @Inject constructor(
    private val repository: StockRepository
) {

    operator fun invoke() = repository.getStocks()
}