package com.krystofmacek.marketviz.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.databinding.TradeDialogBinding
import com.krystofmacek.marketviz.ui.fragments.details.DetailsViewModel
import com.krystofmacek.marketviz.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TradeDialog : BottomSheetDialogFragment() {

    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil
            .inflate<TradeDialogBinding>(inflater, R.layout.trade_dialog, container, false)
            .apply {
                this.lifecycleOwner = viewLifecycleOwner
                this.viewModel = detailsViewModel
            }

        subscribeObservers()
        return binding.root
    }

    private fun subscribeObservers() {
        detailsViewModel.inputNumOfShares.observe(viewLifecycleOwner, Observer {
            val numOfShares: Double? = it?.let {
                if (it.isEmpty()) {
                    1.0
                } else {
                    it.toDouble()
                }
            }
            val lastPrice: Double? = detailsViewModel.detailsQuote.value?.lastPrice

            val total: Double = if(numOfShares != null && lastPrice != null ) {
                numOfShares * lastPrice
            } else {
                0.0
            }

            detailsViewModel.totalPrice.value = Utils.round(total)
        })

        detailsViewModel.positionCreated.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it) {
                    dismiss()
                    Toast.makeText(
                        requireContext(),
                        "${detailsViewModel.detailsQuote.value?.symbol} Position Created.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}