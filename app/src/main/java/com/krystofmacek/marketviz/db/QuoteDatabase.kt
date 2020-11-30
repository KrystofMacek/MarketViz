package com.krystofmacek.marketviz.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.krystofmacek.marketviz.db.dao.QuoteDao
import com.krystofmacek.marketviz.model.databasemodels.*

@Database(
    entities = [MarketIndex::class, DetailsQuote::class, WatchlistQuote::class, Position::class, QuoteHistory::class],
    version = 11
)
@TypeConverters(Converters::class)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun getQuoteDao(): QuoteDao
}