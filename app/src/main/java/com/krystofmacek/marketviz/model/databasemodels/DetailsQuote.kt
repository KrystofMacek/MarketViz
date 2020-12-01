package com.krystofmacek.marketviz.model.databasemodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Model to be used when saving and displaying detail of certain quote
 * */

@Entity(tableName = "details_quote_table")
class DetailsQuote (
    @PrimaryKey
    val symbol: String,
    val name: String = "",
    val lastPrice: Double = 0.0,
    val netChange: Double = 0.0,
    val percentageChange: Double = 0.0,
    val serverTimestamp: String = "",
    val open: Double = 0.0,
    val volume: Int = 0,
    val avgVolume: Int = 0,
    val previousClose: Double = 0.0,
    val isEmpty: Boolean = false
): Serializable