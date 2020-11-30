package com.krystofmacek.marketviz.model.networkmodels.marketdata

/** Model class of individual record of quote history */
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