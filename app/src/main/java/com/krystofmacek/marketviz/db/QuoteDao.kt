package com.krystofmacek.marketviz.db

import androidx.room.*
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import com.krystofmacek.marketviz.model.networkmodels.marketdata.Quote
import com.krystofmacek.marketviz.utils.Constants.MARKET_INDEX
import com.krystofmacek.marketviz.utils.Constants.SEARCH_RESULT
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: Quote)

    // Market Index
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketIndex(index: MarketIndex)

    @Query("SELECT * FROM indices_table")
    fun getMarketIndices(): Flow<List<MarketIndex>>




    @Query("DELETE FROM quotes WHERE category = $SEARCH_RESULT")
    suspend fun deleteLastSearch()


    @Query("SELECT * FROM quotes WHERE category = $SEARCH_RESULT")
    fun getSearchedQuote(): Flow<Quote>
}