package com.krystofmacek.marketviz.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "quotes"
)
data class Quote(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
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
    val symbol: String,
    val tradeTimestamp: String,
    val unitCode: String,
    val volume: Int,
    var category: Int?
) : Serializable