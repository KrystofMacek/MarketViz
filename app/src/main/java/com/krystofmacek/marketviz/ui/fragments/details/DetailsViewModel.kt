package com.krystofmacek.marketviz.ui.fragments.details

import androidx.databinding.Bindable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.krystofmacek.marketviz.model.databasemodels.DetailsQuote
import com.krystofmacek.marketviz.repository.MarketDataRepository

class DetailsViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    val detailsQuote: LiveData<DetailsQuote> = repository.getDetailsQuote().asLiveData()

    val inputNumOfShares: MutableLiveData<String> = MutableLiveData("0")
    val totalPrice: MutableLiveData<Double> = MutableLiveData()

    private val _navigateToDialog = MutableLiveData(false)
    val navigateToDialog: LiveData<Boolean>
        get() = _navigateToDialog

    fun toggleDialog() {
        _navigateToDialog.value?.let {
            _navigateToDialog.postValue(!it)
        }
    }
}