package com.krystofmacek.marketviz.model.networkmodels.marketdata


/** Model class of individual quote from market data call */
data class Quote(

    val symbol: String,
    val close: Double,
    val dayCode: String,
    val dollarVolume: Double,
    val fiftyTwoWkHigh: Double,
    val fiftyTwoWkHighDate: String,
    val fiftyTwoWkLow: Double,
    val fiftyTwoWkLowDate: String,
    val flag: String,
    val high: Double,
    val lastPrice: Double,
    val low: Double,
    val mode: String,
    val name: String,
    val netChange: Double,
    val numTrades: Int,
    val `open`: Double,
    val percentChange: Double,
    val previousVolume: Int,
    val serverTimestamp: String,
    val tradeTimestamp: String,
    val unitCode: String,
    val volume: Int,
    val avgVolume: Int,
    val previousClose: Double,
    var category: Int?
)