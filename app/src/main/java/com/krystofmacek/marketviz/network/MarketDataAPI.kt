package com.krystofmacek.marketviz.network

import com.krystofmacek.marketviz.BuildConfig
import com.krystofmacek.marketviz.model.networkmodels.marketdata.HistoryResponse
import com.krystofmacek.marketviz.model.networkmodels.marketdata.QuoteResponse
import com.krystofmacek.marketviz.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarketDataAPI {
    @GET("/getQuote${BuildConfig.MARKET_DATA_API_KEY}")
    suspend fun getQuotes(
        @Query("symbols")
        symbol: String,
        @Query("fields")
        fields: String
    ): Response<QuoteResponse>

    @GET("/getHistory${BuildConfig.MARKET_DATA_API_KEY}")
    suspend fun getHistory(
        @Query("symbol")
        symbol: String,
        @Query("type")
        type: Constants.historyType,
        @Query("startDate")
        startDate: String,
        @Query("endDate")
        endDate: String,
        @Query("order")
        order: Constants.historyOrder
    ): Response<HistoryResponse>
}