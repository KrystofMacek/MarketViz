package com.krystofmacek.marketviz.ui.fragments.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.krystofmacek.marketviz.model.databasemodels.SearchQuoteResult
import com.krystofmacek.marketviz.model.networkmodels.marketdata.Quote
import com.krystofmacek.marketviz.repository.MarketDataRepository

class DetailsViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    val searchedQuote: LiveData<SearchQuoteResult> = repository.getSearchedQuote().asLiveData()

}