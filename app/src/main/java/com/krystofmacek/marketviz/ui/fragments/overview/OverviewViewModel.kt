package com.krystofmacek.marketviz.ui.fragments.overview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krystofmacek.marketviz.repository.MarketDataRepository
import com.krystofmacek.marketviz.utils.IndexListGenerator
import kotlinx.coroutines.launch

class OverviewViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    fun getQuotes() {
        viewModelScope.launch {
            val response = repository.loadIndices()
        }
    }

}