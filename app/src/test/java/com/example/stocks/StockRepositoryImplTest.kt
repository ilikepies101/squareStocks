package com.example.stocks

import com.example.stocks.data.StockApi
import com.example.stocks.data.local.StockDao
import com.example.stocks.data.local.entity.StockEntity
import com.example.stocks.data.remote.dto.StockDto
import com.example.stocks.data.remote.dto.StockListDto
import com.example.stocks.domain.model.Stock
import com.example.stocks.domain.repository.StockRepositoryImpl
import com.example.stocks.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest

import org.junit.Assert.fail
import org.junit.Test
import java.lang.Exception

class StockRepositoryImplTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun stocksRepositoryImpl_loading_and_success()  = runTest {
        val remoteStocks = listOf(StockDto("", 0, 0, "", null, "A"),
        StockDto("", 0, 0, "", null, "B"))

        val cachedStocks = listOf(StockDto("", 0, 0, "", null, "C"),
            StockDto("", 0, 0, "", null, "D"))

        var hasFetchedRemoteStocks = false

        val repository = StockRepositoryImpl(object : StockApi {
            override suspend fun getStocks(): StockListDto {
                hasFetchedRemoteStocks = true
                return StockListDto(
                    remoteStocks
                )
            }
        }, object : StockDao {
            override suspend fun insertStocks(list: List<StockEntity>) {
            }

            override suspend fun deleteStocks(tickers: List<String>) {
            }

            override suspend fun getStocks(): List<StockEntity> {
                if (hasFetchedRemoteStocks) {
                    return remoteStocks.map { it.toStockEntity() }
                }
                return cachedStocks.map { it.toStockEntity() }
            }
        })

        val result = repository.getStocks().toList()

        assert(result.size == 3)
        assert(result[0] is Resource.Loading)

        assert(result[1] is Resource.Loading)
        val intermittentLoading = result[1]
        assert(intermittentLoading is Resource.Loading)

        (intermittentLoading as Resource.Loading).data?.let { stockList ->
            assert(stockList.size == 2)
            assert(stockList[0] == cachedStocks[0].toStockEntity().toStock())
            assert(stockList[1] == cachedStocks[1].toStockEntity().toStock())
        } ?: fail("Intermittent loading data is null")

        assert(result[2] is Resource.Success)
        val stocks = result[2] as Resource.Success<List<Stock>>

        stocks.data?.let { stockList ->
            assert(stockList.size == 2)
            assert(stockList[0] == remoteStocks[0].toStockEntity().toStock())
            assert(stockList[1] == remoteStocks[1].toStockEntity().toStock())
        } ?: fail("Stocks success data is null")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun stocksRepositoryImpl_loading_and_failure()  = runTest {
        val cachedStocks = listOf(StockDto("", 0, 0, "", null, "A"),
            StockDto("", 0, 0, "", null, "B"))
        val repository = StockRepositoryImpl(object : StockApi {
            override suspend fun getStocks(): StockListDto {
                throw Exception()
            }
        }, object : StockDao {
            override suspend fun insertStocks(list: List<StockEntity>) {
            }

            override suspend fun deleteStocks(tickers: List<String>) {
            }

            override suspend fun getStocks(): List<StockEntity> {
                return cachedStocks.map { it.toStockEntity() }
            }
        })

        val result = repository.getStocks().toList()

        assert(result.size == 3)
        assert(result[0] is Resource.Loading)

        val intermittentLoading = result[1]
        assert(intermittentLoading is Resource.Loading)

        (intermittentLoading as Resource.Loading).data?.let { stockList ->
            assert(stockList.size == 2)
            assert(stockList[0] == cachedStocks[0].toStockEntity().toStock())
            assert(stockList[1] == cachedStocks[1].toStockEntity().toStock())
        } ?: fail("Intermittent loading data is null.")

        assert(result[2] is Resource.Error)
        val stocks = result[2] as? Resource.Error<List<Stock>>

        stocks?.data?.let { stockList ->
            assert(stockList.size == 2)
            assert(stockList[0] == cachedStocks[0].toStockEntity().toStock())
            assert(stockList[1] == cachedStocks[1].toStockEntity().toStock())
        } ?: fail("Stocks or data are null")
    }
}