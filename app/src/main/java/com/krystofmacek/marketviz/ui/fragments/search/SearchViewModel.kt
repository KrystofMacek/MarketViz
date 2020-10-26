package com.krystofmacek.marketviz.ui.fragments.search


import androidx.databinding.Bindable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krystofmacek.marketviz.repository.MarketDataRepository


class SearchViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {


    val symbolKeyWord = MutableLiveData<String>()


}