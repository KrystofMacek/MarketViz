package com.krystofmacek.marketviz.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.generated.callback.OnClickListener
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import com.krystofmacek.marketviz.utils.Utils
import kotlinx.android.synthetic.main.item_quote.view.*

class MarketIndexAdapter: RecyclerView.Adapter<MarketIndexAdapter.QuoteViewHolder>() {

    var onItemSelectedListener: OnItemSelectedListener? = null

    inner class QuoteViewHolder(
        itemView: View,
        onItemSelectedListener: OnItemSelectedListener?
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val listener: OnItemSelectedListener? = onItemSelectedListener

        override fun onClick(v: View?) {
            listener?.onItemSelected(adapterPosition)
        }
    }

    /** Recycler View Methods */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                R.layout.item_quote,
                parent,
                false
            ),
            onItemSelectedListener
        )
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val marketIndex = differ.currentList[position]

        holder.itemView.apply {

            ip_symbol.text = marketIndex.symbol
            ip_name.text = marketIndex.name

            val lastPrice = "${marketIndex.lastPrice}"
            ip_lastPrice.text = lastPrice

            val netChange = "${Utils.round(marketIndex.netChange)}"
            ip_netChange.text = netChange

            val percChange = "${marketIndex.percentageChange}%"
            ip_percentChange.text = percChange


            setOnClickListener(holder)
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