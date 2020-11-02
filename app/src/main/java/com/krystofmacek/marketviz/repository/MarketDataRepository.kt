package com.krystofmacek.marketviz.repository

import android.widget.Toast
import com.krystofmacek.marketviz.db.QuoteDao
import com.krystofmacek.marketviz.model.databasemodels.MarketIndex
import com.krystofmacek.marketviz.model.databasemodels.DetailsQuote
import com.krystofmacek.marketviz.model.databasemodels.Position
import com.krystofmacek.marketviz.model.databasemodels.WatchlistQuote
import com.krystofmacek.marketviz.model.networkmodels.autocomplete.Symbols
import com.krystofmacek.marketviz.network.MarketDataService
import com.krystofmacek.marketviz.network.SymbolAutoCompleteService
import com.krystofmacek.marketviz.utils.Constants.LONG_POSITION
import com.krystofmacek.marketviz.utils.Constants.SHORT_POSITION
import com.krystofmacek.marketviz.utils.Utils
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.coroutineContext


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
                    lastPrice = Utils.round(quote.lastPrice),
                    netChange = Utils.round(quote.netChange),
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
            val searchQuoteResult = DetailsQuote(
                symbol = quote.symbol,
                name = quote.name,
                lastPrice = Utils.round(quote.lastPrice),
                netChange = Utils.round(quote.netChange),
                percentageChange = quote.percentChange,
                serverTimestamp = LocalDate.now().toString(),
                previousClose = Utils.round(quote.previousClose),
                open = Utils.round(quote.open),
                volume = quote.volume,
                avgVolume = quote.avgVolume
            )
            quoteDao.clearDetailsTable()
            quoteDao.insertDetailsQuote(searchQuoteResult)
        }
    }

    fun getDetailsQuote(): Flow<DetailsQuote> = quoteDao.getDetailsQuote()

    suspend fun addToWatchlist(quote: DetailsQuote?) {
        quote?.let {
            val watchlistQuote = WatchlistQuote(
                symbol = quote.symbol,
                name = quote.name,
                lastPrice = quote.lastPrice,
                netChange = quote.netChange,
                percentageChange = quote.percentageChange
            )

            quoteDao.insertWatchlistQuote(watchlistQuote)
        }
    }

    /** Portfolio */

    suspend fun longStock(quote: DetailsQuote?, shares: Int) {
        quote?.let {
            val position = Position(
                id = null,
                entryPrice = it.lastPrice,
                lastPrice = it.lastPrice,
                size = shares,
                symbol = it.symbol,
                name = it.name,
                positionType = LONG_POSITION
            )
            quoteDao.insertPosition(position)
        }
    }
    suspend fun shortStock(quote: DetailsQuote?, shares: Int) {
        quote?.let {
            val position = Position(
                id = null,
                entryPrice = it.lastPrice,
                lastPrice = it.lastPrice,
                size = shares,
                symbol = it.symbol,
                name = it.name,
                positionType = SHORT_POSITION
            )
            quoteDao.insertPosition(position)
        }
    }

    fun getPortfolio(): Flow<List<Position>> = quoteDao.getPortfolio()


}