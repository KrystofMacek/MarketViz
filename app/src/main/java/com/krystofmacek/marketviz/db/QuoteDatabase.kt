package com.krystofmacek.marketviz.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import com.krystofmacek.marketviz.model.databasemodels.Position
import com.krystofmacek.marketviz.model.databasemodels.DetailsQuote
import com.krystofmacek.marketviz.model.databasemodels.WatchlistQuote
import com.krystofmacek.marketviz.model.networkmodels.marketdata.Quote

@Database(
    entities = [Quote::class, MarketIndex::class, DetailsQuote::class, WatchlistQuote::class, Position::class],
    version = 8
)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun getQuoteDao(): QuoteDao
}