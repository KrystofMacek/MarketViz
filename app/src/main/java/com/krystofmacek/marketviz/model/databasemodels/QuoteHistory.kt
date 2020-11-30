package com.krystofmacek.marketviz.model.databasemodels

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Model representing price history of specific quotes
 * */

@Entity(tableName = "histories_table")
class QuoteHistory (
    @PrimaryKey
    val symbol: String,
    val records: List<HistoryRecord>
)