package com.krystofmacek.marketviz.model.databasemodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "histories_table")
class QuoteHistory (
    @PrimaryKey
    val symbol: String,
    val records: List<HistoryRecord>
)