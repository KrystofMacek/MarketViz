package com.krystofmacek.marketviz.network

import com.krystofmacek.marketviz.BuildConfig
import com.krystofmacek.marketviz.model.networkmodels.marketdata.HistoryResponse
import com.krystofmacek.marketviz.model.networkmodels.marketdata.QuoteResponse
import com.krystofmacek.marketviz.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarketDataAPI {

    /**
     * @param symbol - quote symbols
     * @param fields - data for each quote
     * */

    @GET("/getQuote${BuildConfig.MARKET_DATA_API_KEY}")
    suspend fun getQuotes(
        @Query("symbols")
        symbol: String,
        @Query("fields")
        fields: String
    ): Response<QuoteResponse>

    /**
     * @param symbol - quote symbols
     * @param type - time-frame of records - minutes, hours, daily..
     * @param startDate - date of first record
     * @param endDate - date of last record
     * @param order - order of records asc/desc
     * */
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