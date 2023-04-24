package com.example.stocks.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stocks.R
import com.example.stocks.databinding.StockItemBinding
import com.example.stocks.domain.model.Stock
import com.example.stocks.util.Extensions.toFormattedCurrencyAmount
import com.example.stocks.util.Extensions.toFormattedDate

class StockAdapter: RecyclerView.Adapter<StockAdapter.ViewHolder>() {
    private var items: List<Stock> = emptyList()

    inner class ViewHolder(itemBinding: StockItemBinding): RecyclerView.ViewHolder(itemBinding.root) {
        val quantity = itemBinding.quantity
        val quantityLabel = itemBinding.quantityLabel
        val quantityGroup = itemBinding.quantityGroup
        val name = itemBinding.name
        val ticker = itemBinding.ticker
        val price = itemBinding.price
        val currency = itemBinding.currency
        val timeStamp = itemBinding.timestamp

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StockItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val currentItem = items[position]

        holder.run {
            currentItem.quantity?.let { quantityAmount ->
                quantityGroup.visibility = View.VISIBLE
                quantityLabel.text = context.resources.getQuantityString(R.plurals.shares, quantityAmount)
                quantity.text = quantityAmount.toString()
            } ?: run {
                holder.quantityGroup.visibility = View.GONE
            }

            name.text = currentItem.name
            ticker.text = currentItem.ticker

            price.text = currentItem.currentPriceCents.toFormattedCurrencyAmount(currentItem.currency)
            currency.text = currentItem.currency

            timeStamp.text = currentItem.currentPriceTimestamp.toFormattedDate()
        }
    }

    fun insert(stocks: List<Stock>) {
        items = stocks
        notifyItemRangeInserted(0, stocks.size)
    }
}