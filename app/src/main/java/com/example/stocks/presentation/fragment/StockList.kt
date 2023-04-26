package com.example.stocks.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.stocks.databinding.FragmentStockListBinding
import com.example.stocks.presentation.StocksViewModel
import com.example.stocks.presentation.adapter.StockAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Stock list fragment that displays any stocks on the users watchlist
 * or owned by the user in a list.
 */
class StockList : Fragment() {

    private var binding: FragmentStockListBinding? = null
    private val adapter: StockAdapter by lazy { StockAdapter() }
    private val viewModel: StocksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentStockListBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.stocks?.adapter = adapter

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.state.collectLatest { state ->
                if (state.stocks.isNotEmpty() || state.watchList.isNotEmpty()) {
                    adapter.update(state.stocks, state.watchList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}