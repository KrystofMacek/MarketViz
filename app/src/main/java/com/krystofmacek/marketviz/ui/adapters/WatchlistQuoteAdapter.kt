package com.krystofmacek.marketviz.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.model.databasemodels.WatchlistQuote
import com.krystofmacek.marketviz.utils.Utils
import kotlinx.android.synthetic.main.item_quote.view.*

class WatchlistQuoteAdapter: RecyclerView.Adapter<WatchlistQuoteAdapter.WatchlistQuoteViewHolder>()  {

    inner class WatchlistQuoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistQuoteViewHolder {
        return WatchlistQuoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_quote,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: WatchlistQuoteViewHolder, position: Int) {
        val quote = differ.currentList[position]

        holder.itemView.apply {
            ip_symbol.text = quote.symbol
            ip_name.text = quote.name

            val lastPrice = "${quote.lastPrice}"
            ip_lastPrice.text = lastPrice

            val netChange = "${Utils.round(quote.netChange)}"
            ip_netChange.text = netChange

            val percChange = "${quote.percentageChange}%"
            ip_percentChange.text = percChange
        }

    }

    override fun getItemCount(): Int = differ.currentList.size


    /** Diff Util */
    private val diffCallback = object : DiffUtil.ItemCallback<WatchlistQuote>() {
        override fun areItemsTheSame(oldItem: WatchlistQuote, newItem: WatchlistQuote): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: WatchlistQuote, newItem: WatchlistQuote): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(list: List<WatchlistQuote>) = differ.submitList(list)

}