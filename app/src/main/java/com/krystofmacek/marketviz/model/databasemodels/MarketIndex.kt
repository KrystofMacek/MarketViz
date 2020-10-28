package com.krystofmacek.marketviz.model.databasemodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "indices_table")
data class MarketIndex (
    @PrimaryKey
    val symbol: String,
    val name: String,
    val lastPrice: Double,
    val netChange: Double,
    val percentageChange: Double
)

