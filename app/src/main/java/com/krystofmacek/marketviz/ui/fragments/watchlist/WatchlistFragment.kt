package com.krystofmacek.marketviz.ui.fragments.watchlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.ui.adapters.OnItemSelectedListener
import com.krystofmacek.marketviz.ui.adapters.WatchlistQuoteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_watchlist.*
import javax.inject.Inject

@AndroidEntryPoint
class WatchlistFragment : Fragment(R.layout.fragment_watchlist), OnItemSelectedListener {


    private val listener = this

    private val watchlistViewModel: WatchlistViewModel by viewModels()

    @Inject
    lateinit var watchlistQuoteAdapter: WatchlistQuoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_watchlist_recycler.apply {
            this.adapter = watchlistQuoteAdapter
            watchlistQuoteAdapter.onItemSelectedListener = listener
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        watchlistViewModel.watchList.observe(viewLifecycleOwner, Observer {
            watchlistQuoteAdapter.submitList(it)
        })
    }

    /** Handle selecting item from recycler view */
    override fun onItemSelected(position: Int) {
        watchlistViewModel.selectedItem.postValue(position)
        displayDialog()
    }

    /** Display dialog to confirm removing stock from watchlist */
    private fun displayDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.wd_title))
            .setMessage(getString(R.string.wd_message))
            .setNegativeButton(resources.getString(R.string.pd_cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(getString(R.string.wd_accpet)) { dialog, _ ->
                watchlistViewModel.removeFromWatchlist()
                dialog.cancel()
            }
            .show()
    }

}