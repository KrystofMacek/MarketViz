package com.krystofmacek.marketviz.repository

import android.util.Log
import com.krystofmacek.marketviz.db.QuoteDao
import com.krystofmacek.marketviz.db.QuoteDatabase
import com.krystofmacek.marketviz.model.QuoteResponse
import com.krystofmacek.marketviz.network.MarketDataAPI
import com.krystofmacek.marketviz.network.MarketDataService
import com.krystofmacek.marketviz.utils.Constants.MARKET_INDEX
import com.krystofmacek.marketviz.utils.Resource
import retrofit2.Response
import javax.inject.Inject


class MarketDataRepository @Inject constructor(
    val marketDataService: MarketDataService,
    val quoteDao: QuoteDao
) {

    suspend fun loadIndices() {
        marketDataService.loadIndices().data?.let {
            for (quote in it.quotes) {
                quoteDao.insertQuote(quote)

                Log.i("Index", quote.toString())
            }
        }
    }



}