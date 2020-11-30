package com.krystofmacek.marketviz.model.networkmodels.marketdata

import com.google.gson.annotations.SerializedName

/** Response from market data API getQuote */
data class QuoteResponse(
    @SerializedName("results")
    val quotes: List<Quote>,
    val status: Status
)