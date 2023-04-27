package com.example.stocks

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.stocks.presentation.StocksViewModel
import com.example.stocks.presentation.fragment.EmptyState
import com.example.stocks.presentation.fragment.ErrorState
import com.example.stocks.presentation.fragment.StockList
import com.example.stocks.presentation.state.StocksViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: StocksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.state.observe(this) { state ->
            clearFragments()
            when (state.stocksViewState) {
                StocksViewState.SUCCESS -> {
                    val content = if (state.shouldShowStockList) StockList() else EmptyState()
                    replaceContentHost(content)
                }
                StocksViewState.LOADING -> {
                    replaceContentHost(StockList())
                }
                StocksViewState.ERROR -> {
                    replaceErrorHost(ErrorState())
                    if (state.shouldShowStockList) {
                        replaceContentHost(StockList())
                    }
                }
            }
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        viewModel.getStocks()
    }

    private fun clearFragments() {
        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().run {
                remove(it)
                commit()
            }
        }
    }

    private fun replaceContentHost(fragmentToDisplay: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.content_host, fragmentToDisplay)
            commit()
        }
    }

    private fun replaceErrorHost(fragmentToDisplay: Fragment) {
        supportFragmentManager.let { manager ->
            manager.beginTransaction().run {
                replace(R.id.error_host, fragmentToDisplay)
                commit()
            }
        }
    }
}