package com.krystofmacek.marketviz.model.networkmodels.marketdata

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName("results")
    val records: List<Record>,
    val status: Status
)