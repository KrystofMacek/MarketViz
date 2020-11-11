package com.krystofmacek.marketviz.db

import androidx.room.*
import com.krystofmacek.marketviz.model.databasemodels.*
import com.krystofmacek.marketviz.model.networkmodels.marketdata.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    /** Market Index */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketIndex(index: MarketIndex)

    @Query("SELECT * FROM indices_table")
    fun getMarketIndices(): Flow<List<MarketIndex>>

    /** Searching a stock - Details F. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailsQuote(quote: DetailsQuote)

    @Query("DELETE FROM details_quote_table")
    suspend fun clearDetailsTable()

    @Query("SELECT * FROM details_quote_table")
    fun getDetailsQuote(): Flow<DetailsQuote>

    /** Add to Watchlist */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistQuote(quote: WatchlistQuote)

    @Query("SELECT * FROM watchlist_table")
    fun getWatchlist(): Flow<List<WatchlistQuote>>

    /** Portfolio */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosition(position: Position)

    @Query("SELECT * FROM positions_table")
    fun getPortfolio(): Flow<List<Position>>

    @Delete
    suspend fun deletePosition(position: Position)

    @Query("UPDATE positions_table SET lastPrice = :lastPrice WHERE symbol = :symbol")
    fun updatePosition(symbol: String, lastPrice: Double)

    @Delete
    suspend fun removeFromWatchlist(watchlistQuote: WatchlistQuote)

    /** Quote History */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuoteHistory(quoteHistory: QuoteHistory)

    @Query("UPDATE histories_table SET records = :records WHERE symbol = :symbol")
    fun updateHistory(symbol: String, records: List<HistoryRecord>)


}