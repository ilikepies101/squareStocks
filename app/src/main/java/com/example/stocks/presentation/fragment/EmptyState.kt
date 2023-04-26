package com.example.stocks.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.stocks.databinding.FragmentEmptyStateBinding
import com.example.stocks.presentation.StocksViewModel

/**
 * A Fragment to display the empty state when there are no
 * owned stocks by the user nor any stocks on the users watchlist.
 */
class EmptyState : Fragment() {

    private var binding: FragmentEmptyStateBinding? = null
    private val viewModel: StocksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentEmptyStateBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.addNewStocks?.setOnClickListener {
            viewModel.getStocks()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}