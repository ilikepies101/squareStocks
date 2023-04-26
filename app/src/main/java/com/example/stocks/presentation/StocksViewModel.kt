package com.example.stocks.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocks.domain.model.Stock
import com.example.stocks.domain.use_case.GetStocks
import com.example.stocks.presentation.state.StocksState
import com.example.stocks.presentation.state.ViewState
import com.example.stocks.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksViewModel @Inject constructor(
    private val getStocksUseCase: GetStocks,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableLiveData(StocksState())
    val state: LiveData<StocksState> = _state

    fun getStocks() {
        viewModelScope.launch(dispatcher) {
            getStocksUseCase().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.postValue(StocksState(
                            watchList = fetchWatchlist(result),
                            stocks = fetchStockList(result),
                            viewState = ViewState.SUCCESS
                        ))
                    }

                    is Resource.Loading -> {
                        _state.postValue(StocksState(
                            watchList = fetchWatchlist(result),
                            stocks = fetchStockList(result),
                            viewState = ViewState.LOADING
                        ))
                    }

                    is Resource.Error -> {
                        _state.postValue(StocksState(
                            watchList = fetchWatchlist(result),
                            stocks = fetchStockList(result),
                            viewState = ViewState.ERROR
                        ))
                    }
                }
            }.launchIn(this)
        }
    }

    private fun fetchWatchlist(result: Resource<List<Stock>>) =
        result.data?.filter { !ownsStock(it) } ?: emptyList()

    private fun fetchStockList(result: Resource<List<Stock>>) =
        result.data?.filter { ownsStock(it) } ?: emptyList()
    private fun ownsStock(stock: Stock) = stock.quantity != null && stock.quantity != 0
}