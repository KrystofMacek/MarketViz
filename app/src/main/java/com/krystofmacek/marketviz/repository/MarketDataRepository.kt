package com.krystofmacek.marketviz.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
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

    fun getWatchlist(): Flow<List<WatchlistQuote>> = quoteDao.getWatchlist()

    suspend fun removeFromWatchlist(watchlistQuote: WatchlistQuote) {
        quoteDao.removeFromWatchlist(watchlistQuote)
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

    suspend fun closePosition(position: Position) {
        quoteDao.deletePosition(position)
    }

    suspend fun updatePortfolioData() {
        var quotes = ""

        quoteDao.getPortfolio().collect {
            for (i in it) {
                quotes += "${i.symbol},"
            }
            Log.i("UpdatePOrtf", "quotes: $quotes")
        }

        marketDataService.loadPortfolioData(quotes).data?.let { response ->
            for (quote in response.quotes) {
                quoteDao.updatePosition(quote.symbol, quote.lastPrice)
            }
        }
    }



}