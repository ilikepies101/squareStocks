package com.example.stocks.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import com.example.stocks.databinding.FragmentLoadingStocksBinding

/**
 * Loading Fragment displayed while fetching stocks
 */
class LoadingStocks : Fragment() {

    private var binding: FragmentLoadingStocksBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentLoadingStocksBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.moneyWings?.startAnimation(
            RotateAnimation(
                0f, 359f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                duration = 800
                interpolator = AccelerateDecelerateInterpolator()
                repeatCount = Animation.INFINITE
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}