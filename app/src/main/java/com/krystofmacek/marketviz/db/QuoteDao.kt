package com.krystofmacek.marketviz.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.krystofmacek.marketviz.model.Quote
import com.krystofmacek.marketviz.utils.Constants.MARKET_INDEX
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: Quote)

    @Query("SELECT * FROM quotes WHERE category = $MARKET_INDEX")
    fun getAllIndices(): Flow<List<Quote>>
}