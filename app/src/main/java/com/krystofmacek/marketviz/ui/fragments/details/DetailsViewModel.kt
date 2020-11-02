package com.krystofmacek.marketviz.ui.fragments.details

import android.content.Context
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.krystofmacek.marketviz.model.databasemodels.DetailsQuote
import com.krystofmacek.marketviz.repository.MarketDataRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailsViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    val detailsQuote: LiveData<DetailsQuote> = repository.getDetailsQuote().asLiveData()

    val inputNumOfShares: MutableLiveData<String> = MutableLiveData("0")
    val totalPrice: MutableLiveData<Double> = MutableLiveData()

    private val _navigateToDialog = MutableLiveData(false)
    val navigateToDialog: LiveData<Boolean>
        get() = _navigateToDialog

    private val _addToWatchlist = MutableLiveData(false)
    val addToWatchlist: LiveData<Boolean>
        get() = _addToWatchlist

    /** Opens a dialog for creating a position */
    fun toggleDialog() {
        _navigateToDialog.value?.let {
            _navigateToDialog.postValue(!it)
        }
    }

    /** Adds the stock to watchlist */
    fun addToWatchList() {
        viewModelScope.launch {
            repository.addToWatchlist(detailsQuote.value)
            _addToWatchlist.postValue(true)
        }
    }

    fun longStock() {

    }
    fun shortStock() {

    }
}