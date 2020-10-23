package com.krystofmacek.marketviz.ui.fragments.overview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.krystofmacek.marketviz.model.Quote
import com.krystofmacek.marketviz.repository.MarketDataRepository
import com.krystofmacek.marketviz.utils.IndexListGenerator
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class OverviewViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    val marketIndices: LiveData<List<Quote>> = repository.getAllIndices().asLiveData()

    fun getQuotes() {
        viewModelScope.launch {
            val response = repository.loadIndices()
        }
    }



}