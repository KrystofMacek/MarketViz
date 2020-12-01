package com.krystofmacek.marketviz.ui.fragments.search


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.krystofmacek.marketviz.model.networkmodels.autocomplete.Symbols
import com.krystofmacek.marketviz.repository.MarketDataRepository
import kotlinx.coroutines.launch


class SearchViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    val symbolKeyWord = MutableLiveData<String>()

    var autoCompletedList: MutableLiveData<Symbols> = MutableLiveData()

    private val _navigateToDetails = MutableLiveData<Boolean?>()
    val navigateToDetails: LiveData<Boolean?>
        get() = _navigateToDetails

    fun doneNavigating() {
        _navigateToDetails.value = null
    }

    fun getAutoCompleteSymbols(keyword: String) {
        viewModelScope.launch {
            val list = repository.getAutoCompleteSymbolsFor(keyword)
            autoCompletedList.postValue(list)
        }
    }

    fun searchQuote(quote: String) {
        viewModelScope.launch {
            repository.searchQuote(quote)
        }
        _navigateToDetails.value = true
    }
}