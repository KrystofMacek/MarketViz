package com.krystofmacek.marketviz.network

import com.krystofmacek.marketviz.BuildConfig
import com.krystofmacek.marketviz.model.QuoteResponse
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
    ):Response<QuoteResponse>
}