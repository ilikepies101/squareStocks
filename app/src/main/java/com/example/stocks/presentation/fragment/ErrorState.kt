package com.example.stocks.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.stocks.R
import com.example.stocks.databinding.FragmentErrorStateBinding
import com.example.stocks.presentation.StocksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A fragment denoting an error state displayed when there's an exception
 * fetching stocks. Displays a retry button and error details.
 */
class ErrorState : Fragment() {


    private var binding: FragmentErrorStateBinding? = null
    private val viewModel: StocksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentErrorStateBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.errorRetry?.setOnClickListener {
            viewModel.getStocks()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding?.errorTextDetails?.text = if (state.shouldShowStockList) {
                 getString(R.string.error_text_some_data)
            } else {
                getString(R.string.error_text_no_data)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}