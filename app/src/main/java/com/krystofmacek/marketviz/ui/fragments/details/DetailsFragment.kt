package com.krystofmacek.marketviz.ui.fragments.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val detailsViewModel: DetailsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil
            .inflate<FragmentDetailsBinding>(inflater, R.layout.fragment_details, container, false)
            .apply {
                this.lifecycleOwner = viewLifecycleOwner
                this.viewModel = detailsViewModel
            }


        subscribeObservers()

        return binding.root
    }

    private fun subscribeObservers() {
        detailsViewModel.navigateToDialog.observe(viewLifecycleOwner, Observer {
            Log.i("DialogToggle", "navigate to dialog = '${it}'")
            if(it) {
                this.findNavController().navigate(
                    DetailsFragmentDirections.actionDetailsFragmentToTradeDialog()
                )
                detailsViewModel.toggleDialog()
            }
        })

        detailsViewModel.addToWatchlist.observe(viewLifecycleOwner, Observer {
            if(it) {
                Toast.makeText(
                    requireContext(),
                    "${detailsViewModel.detailsQuote.value?.symbol} Added to Watchlist.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        detailsViewModel.positionCreated.observe(viewLifecycleOwner, Observer {
            if(it) {
                Toast.makeText(
                    requireContext(),
                    "${detailsViewModel.detailsQuote.value?.symbol} Position Created.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

}