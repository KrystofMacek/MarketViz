package com.krystofmacek.marketviz.ui.fragments.details

import android.content.Context
import android.util.Log
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

    private val _positionCreated = MutableLiveData(false)
    val positionCreated: LiveData<Boolean>
        get() = _positionCreated

    /** Opens a dialog for creating a position */
    fun toggleDialog() {
        _navigateToDialog.postValue(_navigateToDialog.value?.not())
    }

    /** Adds the stock to watchlist */
    fun addToWatchList() {
        viewModelScope.launch {
            repository.addToWatchlist(detailsQuote.value)
            toggleAddedToWatchlist()
        }
    }

    fun longStock() {
        inputNumOfShares.value?.let {
            try {
                val shares = it.toInt()
                if(shares > 0) {
                    viewModelScope.launch {
                        repository.longStock(detailsQuote.value, shares)
                        togglePositionCreated()
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

    }

    fun shortStock() {
        inputNumOfShares.value?.let {
            try {
                val shares = it.toInt()
                if(shares > 0) {
                    viewModelScope.launch {
                        repository.shortStock(detailsQuote.value, shares)
                        togglePositionCreated()
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private fun togglePositionCreated() {
        _positionCreated.value?.let {
            _positionCreated.postValue(!it)
        }
    }
    private fun toggleAddedToWatchlist() {
        _addToWatchlist.value?.let {
            _addToWatchlist.postValue(!it)
        }
    }
}