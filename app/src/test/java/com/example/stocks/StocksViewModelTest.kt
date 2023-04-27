package com.example.stocks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.stocks.domain.model.Stock
import com.example.stocks.domain.repository.StockRepository
import com.example.stocks.domain.use_case.GetStocks
import com.example.stocks.presentation.StocksViewModel
import com.example.stocks.presentation.state.StocksViewState
import com.example.stocks.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Unit tests for StocksViewModel
 */
@RunWith(JUnit4::class)
class StocksViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun stocksViewModel_Success_NoStocks_NoWatchlist()  {
        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
            override fun getStocks(): Flow<Resource<List<Stock>>> {
                return flowOf(Resource.Success(emptyList()))
            }
        }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks.isEmpty())
            assert(watchList.isEmpty())
            assert(StocksViewState.SUCCESS.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }

    @Test
    fun stocksViewModel_Success_NoStocks_WithWatchlist()  {
        val watchlistStocks = listOf(
            Stock("", 0, 0, "", 0, ""),
            Stock("", 0, 0, "", null, "")
        )

        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
                override fun getStocks(): Flow<Resource<List<Stock>>> {
                    return flowOf(Resource.Success(watchlistStocks))
                }
            }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks.isEmpty())
            assert(watchList == watchlistStocks)
            assert(StocksViewState.SUCCESS.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }

    @Test
    fun stocksViewModel_Success_WithStocks_NoWatchlist()  {
        val stocksList = listOf(
            Stock("", 0, 0, "", 1, ""),
            Stock("", 0, 0, "", 100, "")
        )

        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
                override fun getStocks(): Flow<Resource<List<Stock>>> {
                    return flowOf(Resource.Success(stocksList))
                }
            }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks == stocksList)
            assert(watchList.isEmpty())
            assert(StocksViewState.SUCCESS.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }

    @Test
    fun stocksViewModel_Success_WithStocks_WithWatchlist()  {
        val stocksList = listOf(
            Stock("", 0, 0, "", 1, ""),
            Stock("", 0, 0, "", 100, "")
        )

        val watchlistStocks = listOf(
            Stock("", 0, 0, "", 0, ""),
            Stock("", 0, 0, "", null, "")
        )

        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
                override fun getStocks(): Flow<Resource<List<Stock>>> {
                    return flowOf(Resource.Success(stocksList + watchlistStocks))
                }
            }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks == stocksList)
            assert(watchList == watchlistStocks)
            assert(StocksViewState.SUCCESS.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }

    @Test
    fun stocksViewModel_Loading_NoStocks_NoWatchlist()  {
        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
                override fun getStocks(): Flow<Resource<List<Stock>>> {
                    return flowOf(Resource.Loading(emptyList()))
                }
            }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks.isEmpty())
            assert(watchList.isEmpty())
            assert(StocksViewState.LOADING.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }

    @Test
    fun stocksViewModel_Loading_NoStocks_WithWatchlist()  {
        val watchlistStocks = listOf(
            Stock("", 0, 0, "", 0, ""),
            Stock("", 0, 0, "", null, "")
        )

        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
                override fun getStocks(): Flow<Resource<List<Stock>>> {
                    return flowOf(Resource.Loading(watchlistStocks))
                }
            }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks.isEmpty())
            assert(watchList == watchlistStocks)
            assert(StocksViewState.LOADING.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }

    @Test
    fun stocksViewModel_Loading_WithStocks_NoWatchlist()  {
        val stocksList = listOf(
            Stock("", 0, 0, "", 1, ""),
            Stock("", 0, 0, "", 100, "")
        )

        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
                override fun getStocks(): Flow<Resource<List<Stock>>> {
                    return flowOf(Resource.Loading(stocksList))
                }
            }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks == stocksList)
            assert(watchList.isEmpty())
            assert(StocksViewState.LOADING.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }

    @Test
    fun stocksViewModel_Loading_WithStocks_WithWatchlist()  {
        val stocksList = listOf(
            Stock("", 0, 0, "", 1, ""),
            Stock("", 0, 0, "", 100, "")
        )

        val watchlistStocks = listOf(
            Stock("", 0, 0, "", 0, ""),
            Stock("", 0, 0, "", null, "")
        )

        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
                override fun getStocks(): Flow<Resource<List<Stock>>> {
                    return flowOf(Resource.Loading(stocksList + watchlistStocks))
                }
            }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks == stocksList)
            assert(watchList == watchlistStocks)
            assert(StocksViewState.LOADING.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }

    @Test
    fun stocksViewModel_Error_NoStocks_NoWatchlist()  {
        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
                override fun getStocks(): Flow<Resource<List<Stock>>> {
                    return flowOf(Resource.Error(emptyList()))
                }
            }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks.isEmpty())
            assert(watchList.isEmpty())
            assert(StocksViewState.ERROR.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }

    @Test
    fun stocksViewModel_Error_NoStocks_WithWatchlist()  {
        val watchlistStocks = listOf(
            Stock("", 0, 0, "", 0, ""),
            Stock("", 0, 0, "", null, "")
        )

        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
                override fun getStocks(): Flow<Resource<List<Stock>>> {
                    return flowOf(Resource.Error(watchlistStocks))
                }
            }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks.isEmpty())
            assert(watchList == watchlistStocks)
            assert(StocksViewState.ERROR.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }

    @Test
    fun stocksViewModel_Error_WithStocks_NoWatchlist()  {
        val stocksList = listOf(
            Stock("", 0, 0, "", 1, ""),
            Stock("", 0, 0, "", 100, "")
        )

        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
                override fun getStocks(): Flow<Resource<List<Stock>>> {
                    return flowOf(Resource.Error(stocksList))
                }
            }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks == stocksList)
            assert(watchList.isEmpty())
            assert(StocksViewState.ERROR.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }

    @Test
    fun stocksViewModel_Error_WithStocks_WithWatchlist()  {
        val stocksList = listOf(
            Stock("", 0, 0, "", 1, ""),
            Stock("", 0, 0, "", 100, "")
        )

        val watchlistStocks = listOf(
            Stock("", 0, 0, "", 0, ""),
            Stock("", 0, 0, "", null, "")
        )

        val viewModel = StocksViewModel(
            GetStocks(object : StockRepository {
                override fun getStocks(): Flow<Resource<List<Stock>>> {
                    return flowOf(Resource.Error(stocksList + watchlistStocks))
                }
            }), Dispatchers.Unconfined)

        viewModel.getStocks()

        viewModel.state.value?.run {
            assert(stocks == stocksList)
            assert(watchList == watchlistStocks)
            assert(StocksViewState.ERROR.ordinal == stocksViewState.ordinal)
        } ?: fail("Stocks state was null")
    }
}