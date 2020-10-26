package com.krystofmacek.marketviz.ui.fragments.search


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.krystofmacek.marketviz.model.autocomplete.Symbols
import com.krystofmacek.marketviz.repository.MarketDataRepository
import kotlinx.coroutines.launch


class SearchViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    val symbolKeyWord = MutableLiveData<String>()

    var autoCompletedList: MutableLiveData<Symbols> = MutableLiveData()

    fun getAutoCompleteSymbols(keyword: String) {
        viewModelScope.launch {
            val list = repository.getAutoCompleteSymbolsFor(keyword)
            autoCompletedList.postValue(list)
        }
    }



}