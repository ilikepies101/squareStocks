package com.example.stocks.domain.repository

import com.example.stocks.data.StockApi
import com.example.stocks.data.local.StockDao
import com.example.stocks.domain.model.Stock
import com.example.stocks.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class StockRepositoryImpl(
    private val api: StockApi,
    private val dao: StockDao
) : StockRepository {

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

        } catch (e: HttpException) {
            emit(Resource.Error("An http exception occurred", stocks))
        } catch (e: IOException) {
            emit(Resource.Error("An exception occurred, please check your internet connection.", stocks))
        }

        val newStocks = dao.getStocks().map { it.toStock() }
        emit(Resource.Success(newStocks))
    }
}