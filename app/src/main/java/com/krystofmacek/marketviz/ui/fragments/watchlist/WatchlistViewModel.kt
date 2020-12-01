package com.krystofmacek.marketviz.ui.fragments.watchlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.krystofmacek.marketviz.repository.MarketDataRepository
import kotlinx.coroutines.launch

class WatchlistViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    /** Holds index of selected item */
    val selectedItem = MutableLiveData<Int>()

    /** Holds the watchlist */
    val watchList = repository.getWatchlist().asLiveData()

    /** Handle removing selected item from watchlist */
    fun removeFromWatchlist() {
        watchList.value?.let { list ->
            selectedItem.value?.let { index ->
                val selectedItem = list[index]

                viewModelScope.launch {
                    repository.removeFromWatchlist(selectedItem)
                }
            }
        }
    }
}