package com.krystofmacek.marketviz.ui.fragments.details

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.krystofmacek.marketviz.model.databasemodels.DetailsQuote
import com.krystofmacek.marketviz.repository.MarketDataRepository
import com.krystofmacek.marketviz.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
): ViewModel() {

    val detailsQuote: LiveData<DetailsQuote> = repository.getDetailsQuote().asLiveData()

    val inputNumOfShares: MutableLiveData<String> = MutableLiveData("0")

    val totalPrice: MutableLiveData<Double> = MutableLiveData()

    private val _navigateToDialog = MutableLiveData(false)
    val navigateToDialog: LiveData<Boolean>
        get() = _navigateToDialog

    private val _addToWatchlist = MutableLiveData(false)
    val addToWatchlist: LiveData<Boolean>
        get() = _addToWatchlist

    private val _positionCreated = MutableLiveData(false)
    val positionCreated: LiveData<Boolean>
        get() = _positionCreated

    /** Chart */
    val dataList = MutableLiveData<ArrayList<CandleEntry>>()

    val candleData = MutableLiveData<CandleData>()

    fun loadChart() {

        Log.i("DetailChart", "${detailsQuote.value?.symbol}")
        detailsQuote.value?.symbol?.let { symbol ->
            viewModelScope.launch(Dispatchers.IO) {
                val values = repository.loadHistoryData(symbol)

                if(values.isNotEmpty()) {
                    dataList.postValue(values)
                }
            }
        }
    }

    fun createCandleData(list: ArrayList<CandleEntry>) {
        val dataSet = CandleDataSet(list, "${detailsQuote.value?.symbol}").apply {
            Utils.setupCandlestickDataSet(this)
        }
        candleData.postValue(CandleData(dataSet))
    }


    /** Opens a dialog for creating a position */
    fun toggleDialog() {
        _navigateToDialog.postValue(_navigateToDialog.value?.not())
    }

    /** Adds the stock to watchlist */
    fun addToWatchList() {
        viewModelScope.launch {
            repository.addToWatchlist(detailsQuote.value)
            toggleAddedToWatchlist()
        }
    }

    fun longStock() {
        inputNumOfShares.value?.let {
            try {
                val shares = it.toInt()
                if(shares > 0) {
                    viewModelScope.launch {
                        repository.longStock(detailsQuote.value, shares)
                        togglePositionCreated()
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    fun shortStock() {
        inputNumOfShares.value?.let {
            try {
                val shares = it.toInt()
                if(shares > 0) {
                    viewModelScope.launch {
                        repository.shortStock(detailsQuote.value, shares)
                        togglePositionCreated()
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private fun togglePositionCreated() {
        _positionCreated.value?.let {
            _positionCreated.postValue(!it)
        }
    }

    private fun toggleAddedToWatchlist() {
        _addToWatchlist.value?.let {
            _addToWatchlist.postValue(!it)
        }
    }
}