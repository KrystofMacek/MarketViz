package com.krystofmacek.marketviz.ui.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import com.krystofmacek.marketviz.model.databasemodels.Position
import com.krystofmacek.marketviz.utils.Utils
import kotlinx.android.synthetic.main.item_quote.view.*

class PositionAdapter: RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {

    inner class PositionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PositionViewHolder {
        return PositionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                //TODO: make different layout for Position
                R.layout.item_quote,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PositionViewHolder, position: Int) {
        val quotePosition = differ.currentList[position]

        holder.itemView.apply {
            iq_symbol.text = quotePosition.symbol
            iq_name.text = quotePosition.name

            val lastPrice = "${quotePosition.lastPrice}"
            iq_lastPrice.text = lastPrice

            val netChange = "${Utils.round(quotePosition.entryPrice)}"
            iq_netChange.text = netChange

            val percChange = "${quotePosition.size}%"
            iq_percentChange.text = percChange
        }

    }

    override fun getItemCount(): Int = differ.currentList.size


    /** Diff Util */
    private val diffCallback = object : DiffUtil.ItemCallback<Position>() {
        override fun areItemsTheSame(oldItem: Position, newItem: Position): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Position, newItem: Position): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(list: List<Position>) = differ.submitList(list)

}