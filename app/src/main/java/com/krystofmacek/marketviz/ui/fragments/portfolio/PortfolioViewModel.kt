package com.krystofmacek.marketviz.ui.fragments.portfolio

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.krystofmacek.marketviz.repository.MarketDataRepository

class PortfolioViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
) : ViewModel() {

    val portfolioList = repository.getPortfolio().asLiveData()

}