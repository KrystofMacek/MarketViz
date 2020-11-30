package com.krystofmacek.marketviz.db.dao

import androidx.room.*
import com.krystofmacek.marketviz.model.databasemodels.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    /** Market Index - Overview F */
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

    /** Managing Watchlist - Watchlist F. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistQuote(quote: WatchlistQuote)

    @Query("SELECT * FROM watchlist_table")
    fun getWatchlist(): Flow<List<WatchlistQuote>>

    @Query("SELECT symbol FROM watchlist_table")
    fun getWatchlistSymbols(): List<String>

    @Query("UPDATE watchlist_table SET lastPrice = :lastPrice, netChange = :netChange, percentageChange = :percentageChange WHERE symbol = :symbol")
    fun updateWatchlistQuote(symbol: String, lastPrice: Double, netChange: Double, percentageChange: Double)

    @Delete
    suspend fun removeFromWatchlist(watchlistQuote: WatchlistQuote)

    /** Managing Positions - Portfolio F. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosition(position: Position)

    @Delete
    suspend fun deletePosition(position: Position)

    @Query("UPDATE positions_table SET lastPrice = :lastPrice WHERE symbol = :symbol")
    fun updatePosition(symbol: String, lastPrice: Double)

    @Query("SELECT * FROM positions_table")
    fun getPortfolio(): Flow<List<Position>>

    @Query("SELECT symbol FROM positions_table")
    fun getPortfolioSymbols(): List<String>

    /** Quote History - Details F. Overview F.*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuoteHistory(quoteHistory: QuoteHistory)

    @Query("UPDATE histories_table SET records = :records WHERE symbol = :symbol")
    fun updateHistory(symbol: String, records: List<HistoryRecord>)

    @Query("SELECT * FROM histories_table WHERE symbol = :symbol")
    fun getHistory(symbol: String): QuoteHistory?




}