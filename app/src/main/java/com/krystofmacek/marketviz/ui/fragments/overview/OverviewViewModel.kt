package com.krystofmacek.marketviz.ui.fragments.overview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import com.krystofmacek.marketviz.repository.MarketDataRepository

class OverviewViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    val marketIndices: LiveData<List<MarketIndex>> = repository.getAllIndices().asLiveData()

}