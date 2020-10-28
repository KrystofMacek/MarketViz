package com.krystofmacek.marketviz.repository

import com.krystofmacek.marketviz.db.QuoteDao
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import com.krystofmacek.marketviz.model.databasemodels.SearchQuoteResult
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

    suspend fun searchQuote(keyword: String) {
        marketDataService.searchQuote(keyword).data?.let {
            val quote = it.quotes.first()
            val searchQuoteResult = SearchQuoteResult(
                symbol = quote.symbol,
                name = quote.name,
                lastPrice = quote.lastPrice,
                netChange = quote.netChange,
                percentageChange = quote.percentChange
            )
            quoteDao.clearSearchResultTable()
            quoteDao.insertSearchQuoteResult(searchQuoteResult)
        }
    }

    fun getSearchedQuote(): Flow<SearchQuoteResult> = quoteDao.getSearchedQuote()


}