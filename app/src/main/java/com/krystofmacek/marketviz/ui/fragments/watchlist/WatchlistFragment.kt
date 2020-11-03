package com.krystofmacek.marketviz.ui.fragments.watchlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.ui.adapters.WatchlistQuoteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_watchlist.*
import javax.inject.Inject

@AndroidEntryPoint
class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {


    private val watchlistViewModel: WatchlistViewModel by viewModels()

    @Inject
    lateinit var watchlistQuoteAdapter: WatchlistQuoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_watchlist_recycler.apply {
            this.adapter = watchlistQuoteAdapter
        }

        subscribeObservers()
    }

    private fun subscribeObservers() {
        watchlistViewModel.watchList.observe(viewLifecycleOwner, Observer {
            watchlistQuoteAdapter.submitList(it)
        })
    }

}