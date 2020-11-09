package com.krystofmacek.marketviz.model.databasemodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/** Model to be used when saving and displaying detail of certain quote */

@Entity(tableName = "details_quote_table")
class DetailsQuote (
    @PrimaryKey
    val symbol: String,
    val name: String,
    val lastPrice: Double,
    val netChange: Double,
    val percentageChange: Double,
    val serverTimestamp: String,
    val open: Double,
    val volume: Int,
    val avgVolume: Int,
    val previousClose: Double
): Serializable