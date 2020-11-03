package com.krystofmacek.marketviz.ui.fragments.watchlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.krystofmacek.marketviz.repository.MarketDataRepository

class WatchlistViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    val watchList = repository.getWatchlist().asLiveData()

}