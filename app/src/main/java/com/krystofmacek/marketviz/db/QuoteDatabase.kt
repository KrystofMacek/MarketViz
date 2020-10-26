package com.krystofmacek.marketviz.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.krystofmacek.marketviz.model.marketdata.Quote

@Database(
    entities = [Quote::class],
    version = 4
)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun getQuoteDao(): QuoteDao
}