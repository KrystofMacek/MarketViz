package com.krystofmacek.marketviz.model.databasemodels

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Model to be used when saving and displaying watchlist */

@Entity(tableName = "watchlist_table")
class WatchlistQuote (
    @PrimaryKey
    val symbol: String,
    val name: String,
    val lastPrice: Double,
    val netChange: Double,
    val percentageChange: Double
)