package com.krystofmacek.marketviz.repository

import com.krystofmacek.marketviz.db.QuoteDao
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import com.krystofmacek.marketviz.model.networkmodels.autocomplete.Symbols
import com.krystofmacek.marketviz.model.networkmodels.marketdata.Quote
import com.krystofmacek.marketviz.network.MarketDataService
import com.krystofmacek.marketviz.network.SymbolAutoCompleteService
import com.krystofmacek.marketviz.utils.Constants.MARKET_INDEX
import com.krystofmacek.marketviz.utils.Constants.SEARCH_RESULT
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MarketDataRepository @Inject constructor(
    private val marketDataService: MarketDataService,
    private val symbolAutoCompleteService: SymbolAutoCompleteService,
    private val quoteDao: QuoteDao
) {

    suspend fun loadIndices() {
        marketDataService.loadIndices().data?.let {
            for (quote in it.quotes) {

                val index = MarketIndex(
                    symbol = quote.symbol,
                    name = quote.name,
                    lastPrice = quote.lastPrice,
                    netChange = quote.netChange,
                    percentageChange = quote.percentChange
                )
                quoteDao.insertMarketIndex(index)
            }
        }
    }

    fun getAllIndices(): Flow<List<MarketIndex>> = quoteDao.getMarketIndices()

    suspend fun getAutoCompleteSymbolsFor(keyword: String): Symbols {
        symbolAutoCompleteService.getSymbolsFor(keyword).data?.let {
            return it
        }
        return Symbols()
    }

    suspend fun searchQuote(quote: String) {
        marketDataService.searchQuote(quote).data?.let {
            val quote = it.quotes.first().apply {
                category = SEARCH_RESULT
            }
            quoteDao.insertQuote(quote)
        }
    }

    fun getSearchedQuote(): Flow<Quote> = quoteDao.getSearchedQuote()

    suspend fun deleteLastSearch() {
        quoteDao.deleteLastSearch()
    }

}