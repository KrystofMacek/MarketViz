package com.krystofmacek.marketviz.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import kotlinx.android.synthetic.main.item_quote.view.*
import java.math.BigDecimal
import java.math.RoundingMode

class MarketIndexAdapter: RecyclerView.Adapter<MarketIndexAdapter.QuoteViewHolder>() {

    inner class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /** Recycler View Methods */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                R.layout.item_quote,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val marketIndex = differ.currentList[position]

        holder.itemView.apply {

            iq_symbol.text = marketIndex.symbol
            iq_name.text = marketIndex.name

            val lastPrice = "${marketIndex.lastPrice}"
            iq_lastPrice.text = lastPrice

            val netChange = "${BigDecimal(marketIndex.netChange).setScale(2, RoundingMode.HALF_EVEN)}"
            iq_netChange.text = netChange

            val percChange = "${marketIndex.percentageChange}%"
            iq_percentChange.text = percChange

        }

    }

    override fun getItemCount(): Int = differ.currentList.size


    /** Diff Util */
    private val diffCallback = object : DiffUtil.ItemCallback<MarketIndex>() {
        override fun areItemsTheSame(oldItem: MarketIndex, newItem: MarketIndex): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: MarketIndex, newItem: MarketIndex): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(list: List<MarketIndex>) = differ.submitList(list)

}