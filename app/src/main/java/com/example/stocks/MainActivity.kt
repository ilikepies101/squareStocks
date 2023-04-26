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
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.state.collectLatest { state ->
                val wings = findViewById<TextView>(R.id.money_wings)
                if (state.isLoading) {

                    findViewById<View>(R.id.empty_state).visibility = View.GONE
                    if (wings.animation?.hasStarted() == true) {
                        return@collectLatest
                    }

                    findViewById<LinearLayout>(R.id.loading).visibility = View.VISIBLE
                    wings.startAnimation(RotateAnimation(
                        0f, 359f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f
                    ).apply {
                        duration = 800
                        interpolator = AccelerateDecelerateInterpolator()
                        repeatCount = Animation.INFINITE
                    })
                    return@collectLatest
                }
                wings.animation = null
                findViewById<LinearLayout>(R.id.loading).visibility = View.GONE

                val shouldShowEmptyState = state.stocks.isEmpty() && state.watchList.isEmpty()
                if (shouldShowEmptyState) {
                    findViewById<View>(R.id.empty_state).visibility = View.VISIBLE
                    findViewById<Button>(R.id.add_new_stocks).run {
                        setOnClickListener {
                            viewModel.getStocks()
                        }
                    }
                    return@collectLatest
                }

                findViewById<View>(R.id.empty_state).visibility = View.GONE
                adapter.update(state.stocks, state.watchList)
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.stocks)
        recyclerView.adapter = adapter

        setSupportActionBar(findViewById(R.id.toolbar))
        viewModel.getStocks()
    }


}