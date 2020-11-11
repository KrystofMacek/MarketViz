package com.krystofmacek.marketviz.model.networkmodels.marketdata

data class Record(
    val close: Double,
    val high: Double,
    val low: Double,
    val `open`: Double,
    val symbol: String,
    val timestamp: String,
    val tradingDay: String,
    val volume: Int
)