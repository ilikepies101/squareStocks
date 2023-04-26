package com.example.stocks.domain.repository

import com.example.stocks.data.StockApi
import com.example.stocks.data.local.StockDao
import com.example.stocks.domain.model.Stock
import com.example.stocks.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val dao: StockDao
) : StockRepository {

    /**
     * Fetch stocks, emitting Resource entities representing the status of the call.
     * Note: Return cached stocks if we encounter an exception when fetching from the api.
     */
    override fun getStocks(): Flow<Resource<List<Stock>>> = flow {
        emit(Resource.Loading())

        val stocks = dao.getStocks().map {
            it.toStock()
        }

        emit(Resource.Loading(stocks))

        try {
            val remoteStocks = api.getStocks()
            dao.deleteStocks(remoteStocks.stocks.map { it.ticker })
            dao.insertStocks(remoteStocks.stocks.map { it.toStockEntity() })
        } catch (e: Exception) {
            emit(Resource.Error(stocks))
            return@flow
        }

        val newStocks = dao.getStocks().map { it.toStock() }
        emit(Resource.Success(newStocks))
    }
}