package com.krystofmacek.marketviz.model

import com.google.gson.annotations.SerializedName

data class QuoteResponse(
    //map the results JSON field to quotes field
    @SerializedName("results")
    val quotes: List<Quote>,
    val status: Status
)