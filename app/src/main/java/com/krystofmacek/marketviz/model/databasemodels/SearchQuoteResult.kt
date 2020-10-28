package com.krystofmacek.marketviz.model.databasemodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searched_quote_table")
class SearchQuoteResult (
    @PrimaryKey
    val symbol: String,
    val name: String,
    val lastPrice: Double,
    val netChange: Double,
    val percentageChange: Double
)