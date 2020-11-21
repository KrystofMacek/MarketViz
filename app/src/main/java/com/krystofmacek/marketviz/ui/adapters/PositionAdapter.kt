package com.krystofmacek.marketviz.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.model.databasemodels.Position
import com.krystofmacek.marketviz.utils.Constants.LONG_POSITION
import com.krystofmacek.marketviz.utils.Utils
import kotlinx.android.synthetic.main.item_position.view.*
import kotlinx.android.synthetic.main.item_quote.view.*
import kotlinx.android.synthetic.main.item_quote.view.ip_name
import kotlinx.android.synthetic.main.item_quote.view.ip_netChange
import kotlinx.android.synthetic.main.item_quote.view.ip_percentChange
import kotlinx.android.synthetic.main.item_quote.view.ip_symbol

class PositionAdapter: RecyclerView.Adapter<PositionAdapter.PositionViewHolder>() {

    var onItemSelectedListener: OnItemSelectedListener? = null

    inner class PositionViewHolder(
        itemView: View,
        onItemSelectedListener: OnItemSelectedListener?
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val listener: OnItemSelectedListener? = onItemSelectedListener
        override fun onClick(v: View?) {
            listener?.onItemSelected(adapterPosition)
        }
    }

    /** Recycler View Methods */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PositionViewHolder {
        return PositionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_position,
                parent,
                false
            ),
            onItemSelectedListener
        )
    }

    override fun onBindViewHolder(
        holder: PositionViewHolder,
        position: Int
    ) {
        val quotePosition = differ.currentList[position]

        holder.itemView.apply {
            ip_symbol.text = quotePosition.symbol
            ip_name.text = quotePosition.name

            val value = "${Utils.round(quotePosition.lastPrice * quotePosition.size)}$"
            ip_value.text = value

            val netDifference = quotePosition.lastPrice - quotePosition.entryPrice

            val netChange = "${Utils.round(netDifference * quotePosition.size)}$"
            ip_netChange.text = netChange

            val percChange = "${Utils.round(netDifference / quotePosition.lastPrice)}%"
            ip_percentChange.text = percChange

            ip_type.text = when(quotePosition.positionType) {
                LONG_POSITION -> "LONG"
                else -> "SHORT"
            }

            this.isActivated = netDifference >= 0

            setOnClickListener(holder)
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

    fun getCurrentPL(): Double {
        var pl = 0.0
        for(i in differ.currentList) {
            pl += (i.lastPrice - i.entryPrice) * i.size
        }
        return Utils.round(pl)
    }


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