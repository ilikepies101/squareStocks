package com.example.stocks.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.stocks.R
import com.example.stocks.databinding.StockItemBinding
import com.example.stocks.domain.model.Stock
import com.example.stocks.util.Extensions.toFormattedCurrencyAmount
import com.example.stocks.util.Extensions.toFormattedDate

class StockAdapter: RecyclerView.Adapter<StockAdapter.StockViewHolder>() {
    private var items: List<Stock> = emptyList()

    inner class StockViewHolder(itemBinding: StockItemBinding) : ViewHolder(itemBinding.root) {
        val quantity = itemBinding.quantity
        val quantityGroup = itemBinding.quantityGroup
        val name = itemBinding.name
        val ticker = itemBinding.ticker
        val price = itemBinding.price
        val timeStamp = itemBinding.timestamp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder(StockItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val context = holder.itemView.context
        val currentItem = items[position]

        holder.run {
            currentItem.quantity?.let { quantityAmount ->
                quantity.text = quantityAmount.toString()
                quantityGroup.visibility = View.VISIBLE
            } ?: run {
                quantityGroup.visibility = View.GONE
            }

            name.text = currentItem.name
            ticker.text = currentItem.ticker

            price.text = currentItem.currentPriceCents.toFormattedCurrencyAmount(currentItem.currency)
            timeStamp.text = context.getString(R.string.last_updated_text, currentItem.currentPriceTimestamp.toFormattedDate())
        }
    }
    fun insert(stocks: List<Stock>) {
        items = stocks
        notifyItemRangeInserted(0, stocks.size)
    }
}