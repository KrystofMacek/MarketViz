package com.krystofmacek.marketviz.ui.fragments.overview

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import com.krystofmacek.marketviz.repository.MarketDataRepository
import com.krystofmacek.marketviz.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OverviewViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    val marketIndices: LiveData<List<MarketIndex>> = repository.getAllIndices().asLiveData()

    val selectedItem = MutableLiveData<Int>()
    private val selectedItemSymbol = MutableLiveData<String>()

    val dataList = MutableLiveData<ArrayList<CandleEntry>>()

    val candleData = MutableLiveData<CandleData>()

    fun loadChart(it: Int) {
        marketIndices.value?.get(it)?.symbol?.let { symbol ->

            selectedItemSymbol.postValue(symbol)
            viewModelScope.launch(Dispatchers.IO) {
                val values = repository.loadHistoryData(symbol)
                if(values.isNotEmpty()) {
                    dataList.postValue(values)
                }
            }
        }
    }

    fun createCandleData(list: ArrayList<CandleEntry>) {
        val dataSet = CandleDataSet(list, selectedItemSymbol.value).apply {
            Utils.setupCandlestickDataSet(this)
        }
        candleData.postValue(CandleData(dataSet))
    }
}