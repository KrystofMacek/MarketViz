package com.krystofmacek.marketviz.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.model.networkmodels.marketdata.Quote
import kotlinx.android.synthetic.main.item_quote.view.*
import java.math.BigDecimal
import java.math.RoundingMode

class QuoteAdapter: RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

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
        val quote = differ.currentList[position]

        holder.itemView.apply {

            iq_symbol.text = quote.symbol
            iq_name.text = quote.name

            val lastPrice = "${quote.lastPrice}"
            iq_lastPrice.text = lastPrice

            val netChange = "${BigDecimal(quote.netChange).setScale(2, RoundingMode.HALF_EVEN)}"
            iq_netChange.text = netChange

            val percChange = "${quote.percentChange}%"
            iq_percentChange.text = percChange

        }

    }

    override fun getItemCount(): Int = differ.currentList.size


    /** Diff Util */
    private val diffCallback = object : DiffUtil.ItemCallback<Quote>() {
        override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(list: List<Quote>) = differ.submitList(list)

}