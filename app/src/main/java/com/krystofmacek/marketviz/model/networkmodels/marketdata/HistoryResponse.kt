package com.krystofmacek.marketviz.model.networkmodels.marketdata

import com.google.gson.annotations.SerializedName

/** Response from market data API getHistory */
data class HistoryResponse(
    @SerializedName("results")
    val records: List<Record>,
    val status: Status
)