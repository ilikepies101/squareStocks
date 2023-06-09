package com.example.stocks.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.stocks.R
import com.example.stocks.databinding.HeaderItemBinding
import com.example.stocks.databinding.StockItemBinding
import com.example.stocks.domain.model.Stock
import com.example.stocks.util.Extensions.toFormattedCurrencyAmount
import com.example.stocks.util.Extensions.toFormattedDate

/**
 * Adapter for displaying stock list including stocks that are on
 * a user's watchlist and a user's owned stocks. Also displays header items
 * at the top of each section.
 */
class StockAdapter: RecyclerView.Adapter<ViewHolder>() {
    private var myStocks: List<Stock> = emptyList()
    private var myWatchlist: List<Stock> = emptyList()
    private var myStocksHeaderCount = 0
    private var myWatchlistHeaderCount = 0

    /**
     * View holder for stock item
     */
    inner class StockViewHolder(itemBinding: StockItemBinding) : ViewHolder(itemBinding.root) {
        val quantity = itemBinding.quantity
        val quantityGroup = itemBinding.quantityGroup
        val name = itemBinding.name
        val ticker = itemBinding.ticker
        val price = itemBinding.price
        val timeStamp = itemBinding.timestamp
    }

    /**
     * View holder for header of "My Stocks" section of list
     */
    inner class MyStocksHeaderViewHolder(itemBinding: HeaderItemBinding) : ViewHolder(itemBinding.root) {
        val header = itemBinding.headerTitle
    }

    /**
     * View holder for header of "My Watchlist" section of list
     */
    inner class MyWatchlistHeaderViewHolder(itemBinding: HeaderItemBinding) : ViewHolder(itemBinding.root) {
        val header = itemBinding.headerTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewType.MY_STOCKS_HEADER.ordinal -> {
                MyStocksHeaderViewHolder(HeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            ViewType.MY_WATCHLIST_HEADER.ordinal -> {
                MyWatchlistHeaderViewHolder(HeaderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                StockViewHolder(StockItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    /**
     * Account for items of stocks, watchlist, and header items if needed.
     */
    override fun getItemCount() =
        myStocks.size + myWatchlist.size + myStocksHeaderCount + myWatchlistHeaderCount

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context

        (holder as? MyStocksHeaderViewHolder)?.run {
            header.text = holder.itemView.context.getString(R.string.my_stocks)
        }

        (holder as? MyWatchlistHeaderViewHolder)?.run {
            header.text = holder.itemView.context.getString(R.string.my_watchlist)
        }

        (holder as? StockViewHolder)?.run {
            val currentItem = if (position <= myStocks.size) {
                myStocks[position - 1]
            } else {
                myWatchlist[position - myStocks.size - myStocksHeaderCount - 1]
            }

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

    /**
     * Completely refresh list if any of the items in the list are different.
     */
    fun update(stocks: List<Stock>, watchList: List<Stock>) {
        if (stocks != myStocks || watchList != myWatchlist) {
            myStocks = stocks
            myWatchlist = watchList

            myStocksHeaderCount = if (stocks.isEmpty()) 0 else 1
            myWatchlistHeaderCount = if (watchList.isEmpty()) 0 else 1

            notifyDataSetChanged()
        }
    }

    /**
     * Order of the list should be
     * MY_STOCKS_HEADER
     * MY_STOCKS
     * MY_WATCHLIST_HEADER
     * MY_WATCHLIST
     * assuming there are watchlist items and stock items
     */
    override fun getItemViewType(position: Int): Int {
        if (myStocks.isNotEmpty()) {
            if (position == 0) {
                return ViewType.MY_STOCKS_HEADER.ordinal
            } else if (position <= myStocks.size - 1 + myStocksHeaderCount) {
                return ViewType.MY_STOCK.ordinal
            }
        }

        val myWatchlistOffset = myStocks.size + myStocksHeaderCount
        if (myWatchlist.isNotEmpty()) {
            return if (position - myWatchlistOffset == 0) {
                ViewType.MY_WATCHLIST_HEADER.ordinal
            } else {
                ViewType.MY_WATCHLIST.ordinal
            }
        }

        return -1
    }

    enum class ViewType {
        MY_STOCKS_HEADER, MY_STOCK, MY_WATCHLIST_HEADER, MY_WATCHLIST
    }
}