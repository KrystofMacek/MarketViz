package com.krystofmacek.marketviz.ui.fragments.portfolio

import android.content.Context
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.krystofmacek.marketviz.model.databasemodels.Position
import com.krystofmacek.marketviz.repository.MarketDataRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

class PortfolioViewModel @ViewModelInject constructor(
    private val repository: MarketDataRepository
) : ViewModel() {

    val portfolioList = repository.getPortfolio().asLiveData()

    val currentPL = MutableLiveData<Double>()

    val numOfPositions = MutableLiveData<Int>()


    private val _loadTotalPL = MutableLiveData(true)
    val loadTotalPL: LiveData<Boolean>
        get() = _loadTotalPL

    private val _updateTotalPL = MutableLiveData(false)
    val updateTotalPL: LiveData<Boolean>
        get() = _updateTotalPL

    val totalPL = MutableLiveData<Double>()


    val selectedItem = MutableLiveData<Int>()

    fun closePosition() {
        Log.i("plTotal", "closing position")

        portfolioList.value?.let { list ->
            selectedItem.value?.let { index ->
                val position = list[index]

                totalPL.value?.let { totalPlVal ->
                    val newPl = totalPlVal + ((position.lastPrice - position.entryPrice) * position.size).toFloat()
                    totalPL.postValue(newPl)
                }

                viewModelScope.launch {
                    repository.closePosition(position)
                }
                _updateTotalPL.postValue(true)

            }
        }
    }

    fun totalPlUpdateFinished() {
        _updateTotalPL.postValue(false)
    }

    fun totalPlLoaded() {
        _loadTotalPL.postValue(false)
    }


}