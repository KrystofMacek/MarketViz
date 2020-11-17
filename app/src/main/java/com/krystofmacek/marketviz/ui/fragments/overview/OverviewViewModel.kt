package com.krystofmacek.marketviz.ui.fragments.overview

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import com.krystofmacek.marketviz.repository.MarketDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OverviewViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    val marketIndices: LiveData<List<MarketIndex>> = repository.getAllIndices().asLiveData()

    val selectedItem = MutableLiveData<Int>()

    val dataList = MutableLiveData<ArrayList<CandleEntry>>()

    val candleData = MutableLiveData<CandleData>()


    fun loadChart(it: Int) {

        Log.i("loadChart", "loadChart()")
        marketIndices.value?.get(it)?.symbol?.let { symbol ->

            Log.i("loadChart", "$symbol")

            viewModelScope.launch(Dispatchers.IO) {
                val values = repository.loadHistoryData(symbol)
                dataList.postValue(values)
            }
        }
    }

    fun createCandleData(list: ArrayList<CandleEntry>) {

        val dataSet = CandleDataSet(list, "DataSet").apply {
            color = Color.rgb(80, 80, 80)
            shadowColor = Color.GRAY
            shadowWidth = 0.8f

            decreasingColor = Color.RED
            decreasingPaintStyle = Paint.Style.FILL

            increasingColor = Color.GREEN
            increasingPaintStyle = Paint.Style.FILL

            neutralColor = Color.GRAY

            setDrawValues(false)
        }

        candleData.postValue(CandleData(dataSet))

    }

}