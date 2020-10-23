package com.krystofmacek.marketviz.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.krystofmacek.marketviz.model.Quote

@Database(
    entities = [Quote::class],
    version = 1
)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun getQuoteDao(): QuoteDao
}