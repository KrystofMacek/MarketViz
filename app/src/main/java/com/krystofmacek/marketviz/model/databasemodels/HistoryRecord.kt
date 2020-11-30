package com.krystofmacek.marketviz.model.databasemodels

/**
 * Represents one record (candleStick) of quote history
 * */
class HistoryRecord (
    val close: Double,
    val high: Double,
    val low: Double,
    val open: Double,
    val timestamp: String,
    val tradingDay: String,
    val volume: Int
)