package com.example.stocks

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.stocks.presentation.StocksViewModel
import com.example.stocks.presentation.adapter.StockAdapter
import com.example.stocks.presentation.ui.ViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: StocksViewModel by viewModels()
    private val adapter: StockAdapter by lazy { StockAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val stocksList = findViewById<RecyclerView>(R.id.stocks)
        findViewById<Button>(R.id.add_new_stocks).setOnClickListener {
            viewModel.getStocks()
        }

        findViewById<Button>(R.id.error_retry).setOnClickListener {
            viewModel.getStocks()
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.state.collectLatest { state ->
                val wings = findViewById<TextView>(R.id.money_wings)
                val loading = findViewById<LinearLayout>(R.id.loading)
                val emptyState = findViewById<View>(R.id.empty_state)
                val errorTextDetails = findViewById<TextView>(R.id.error_text_details)
                val errorState = findViewById<View>(R.id.error_state)

                when (state.viewState) {
                    ViewState.SUCCESS -> {
                        wings.animation = null
                        loading.visibility = View.GONE
                        errorState.visibility = View.GONE

                        if (state.stocks.isNotEmpty() && state.watchList.isNotEmpty()) {
                            stocksList.visibility = View.VISIBLE
                            adapter.update(state.stocks, state.watchList)
                        } else {
                            stocksList.visibility = View.GONE
                            emptyState.visibility = View.VISIBLE
                        }
                    }
                    ViewState.LOADING -> {
                        emptyState.visibility = View.GONE
                        errorState.visibility = View.GONE

                        loading.visibility = View.VISIBLE
                        if (wings.animation?.hasStarted() == true) {
                            return@collectLatest
                        }

                        wings.startAnimation(RotateAnimation(
                            0f, 359f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f
                        ).apply {
                            duration = 800
                            interpolator = AccelerateDecelerateInterpolator()
                            repeatCount = Animation.INFINITE
                        })
                    }
                    ViewState.ERROR -> {
                        wings.animation = null
                        loading.visibility = View.GONE
                        emptyState.visibility = View.GONE
                        errorState.visibility = View.VISIBLE

                         if (state.stocks.isNotEmpty() || state.watchList.isNotEmpty()) {
                             errorTextDetails.text = getString(R.string.error_text_some_data)
                             stocksList.visibility = View.VISIBLE
                             adapter.update(state.stocks, state.watchList)
                        } else {
                             errorTextDetails.text = getString(R.string.error_text_no_data)
                        }
                    }
                }
            }
        }

        stocksList.adapter = adapter
        setSupportActionBar(findViewById(R.id.toolbar))
        viewModel.getStocks()
    }
}