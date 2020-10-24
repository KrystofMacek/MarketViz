package com.krystofmacek.marketviz.repository

import android.util.Log
import com.krystofmacek.marketviz.db.QuoteDao
import com.krystofmacek.marketviz.model.Quote
import com.krystofmacek.marketviz.network.MarketDataService
import com.krystofmacek.marketviz.utils.Constants.MARKET_INDEX
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


class MarketDataRepository @Inject constructor(
    private val marketDataService: MarketDataService,
    private val quoteDao: QuoteDao
) {

    suspend fun loadIndices() {
        marketDataService.loadIndices().data?.let {
            for (quote in it.quotes) {
                quote.category = MARKET_INDEX
                quoteDao.insertQuote(quote)
            }
        }
    }

    // TODO: Create seperate quote model for DB and for Network
    fun getAllIndices(): Flow<List<Quote>> = quoteDao.getAllIndices()




}