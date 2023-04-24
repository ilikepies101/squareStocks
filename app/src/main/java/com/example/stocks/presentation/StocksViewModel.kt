package com.example.stocks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocks.domain.use_case.GetStocks
import com.example.stocks.presentation.ui.StocksState
import com.example.stocks.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksViewModel @Inject constructor(
    private val getStocksUseCase: GetStocks
): ViewModel() {

    private val _state = MutableStateFlow(StocksState())
    val state = _state.asStateFlow()

    fun getStocks() {
        viewModelScope.launch {
            getStocksUseCase().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            stocks = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            stocks = result.data ?: emptyList(),
                            isLoading = true
                        )
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            stocks = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}