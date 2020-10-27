package com.krystofmacek.marketviz.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.model.autocomplete.SymbolsItem
import kotlinx.android.synthetic.main.item_autocomplete.view.*

class AutoCompleteAdapter(
): RecyclerView.Adapter<AutoCompleteAdapter.AutoCompleteViewHolder>() {

    var onItemSelectedListener: OnItemSelectedListener? = null

    inner class AutoCompleteViewHolder(itemView: View, onItemSelectedListener: OnItemSelectedListener?)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val listener: OnItemSelectedListener? = onItemSelectedListener

        override fun onClick(v: View?) {
            listener?.onItemSelected(adapterPosition)
        }
    }


    /** Recycler View Methods */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AutoCompleteViewHolder {
        return AutoCompleteViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(
                R.layout.item_autocomplete,
                parent,
                false
            ),
            onItemSelectedListener
        )
    }


    override fun onBindViewHolder(
        holder: AutoCompleteViewHolder,
        position: Int
    ) {
        val item = differ.currentList[position]

        holder.itemView.apply {

            var name = item.name
            if (name.length > 22) {
                name = "${name.substring(0, 22)}."
            }

            ia_name.text = name
            ia_symbol.text = item.symbol
        }
        holder.itemView.setOnClickListener(holder)

    }

    override fun getItemCount(): Int = differ.currentList.size


    /** Diff Util */
    private val diffCallback = object : DiffUtil.ItemCallback<SymbolsItem>() {
        override fun areItemsTheSame(oldItem: SymbolsItem, newItem: SymbolsItem): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: SymbolsItem, newItem: SymbolsItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    fun submitList(list: List<SymbolsItem>) = differ.submitList(list)


    interface OnItemSelectedListener {
        fun onItemSelected(position: Int)
    }

}