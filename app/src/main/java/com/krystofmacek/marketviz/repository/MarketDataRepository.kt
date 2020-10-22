package com.krystofmacek.marketviz.repository

import com.krystofmacek.marketviz.network.MarketDataAPI
import javax.inject.Inject

class MarketDataRepository @Inject constructor(
    val api: MarketDataAPI
) {
    // Market Data Api
    suspend fun getQuotes(quotes: String = "DJIA", fields: String = "fiftyTwoWkHigh,fiftyTwoWkHighDate,fiftyTwoWkLow,fiftyTwoWkLowDate") =
        api.getQuotes(quotes, fields)
}