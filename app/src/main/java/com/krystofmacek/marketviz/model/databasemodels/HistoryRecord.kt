package com.krystofmacek.marketviz.model.databasemodels

class HistoryRecord (
    val close: Double,
    val high: Double,
    val low: Double,
    val open: Double,
    val timestamp: String,
    val tradingDay: String,
    val volume: Int
)