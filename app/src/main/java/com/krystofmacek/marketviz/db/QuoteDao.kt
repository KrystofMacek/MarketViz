package com.krystofmacek.marketviz.db

import androidx.room.*
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import com.krystofmacek.marketviz.model.databasemodels.DetailsQuote
import com.krystofmacek.marketviz.model.networkmodels.marketdata.Quote
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

    // Search
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailsQuote(result: DetailsQuote)

    @Query("DELETE FROM details_quote_table")
    suspend fun clearDetailsTable()

    @Query("SELECT * FROM details_quote_table")
    fun getDetailsQuote(): Flow<DetailsQuote>
}