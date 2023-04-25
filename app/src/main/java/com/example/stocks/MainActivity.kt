package com.example.stocks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.stocks.presentation.StocksViewModel
import com.example.stocks.presentation.adapter.StockAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: StocksViewModel by viewModels()
    private val adapter: StockAdapter by lazy { StockAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                if (state.isLoading) {
                    return@collectLatest
                }

                adapter.insert(state.stocks, state.watchList)
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.stocks)
        recyclerView.adapter = adapter

        setSupportActionBar(findViewById(R.id.toolbar))



        viewModel.getStocks()
    }


}