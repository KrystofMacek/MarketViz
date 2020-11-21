package com.krystofmacek.marketviz.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.CandleEntry
import com.krystofmacek.marketviz.db.QuoteDao
import com.krystofmacek.marketviz.model.databasemodels.*
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

    /** Load data for market indices */
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
                // get history for each symbol
                this.getHistory(quote.symbol)
            }
        }
    }

    /** get indices from database */
    fun getAllIndices(): Flow<List<MarketIndex>> = quoteDao.getMarketIndices()

    /** get autocomplete data for keyword */
    suspend fun getAutoCompleteSymbolsFor(keyword: String): Symbols {
        symbolAutoCompleteService.getSymbolsFor(keyword).data?.let {
            return it
        }
        return Symbols()
    }

    /** get data for specified Quote */
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

            this.getHistory(keyword)

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
        quoteDao.getPortfolioSymbols().let {
            var quotes = ""
            for (i in it) {
                quotes += "$i,"
            }
            loadPortfolioData(quotes)
        }

    }
    private suspend fun loadPortfolioData(quotes: String) {
        marketDataService.loadPortfolioData(quotes).data?.let { response ->
            for (quote in response.quotes) {
                Log.i("load portfolio data", "quote last price: ${quote.lastPrice}")
                quoteDao.updatePosition(quote.symbol, quote.lastPrice)
            }
        }
    }


    /** Quote History */
    suspend fun getHistory(symbol: String) {
        marketDataService.getHistory(symbol).data?.let {

            val historySymbol = it.records[0].symbol

            val historyRecords = mutableListOf<HistoryRecord>()

            for (r in it.records) {
                historyRecords.add(
                    HistoryRecord(
                        open = r.open,
                        close = r.close,
                        high = r.high,
                        low = r.low,
                        volume = r.volume,
                        timestamp = r.timestamp,
                        tradingDay = r.tradingDay
                    )
                )

                Log.i("getHistory", r.toString())
            }

            val quoteHistory = QuoteHistory(
                symbol = historySymbol,
                records = historyRecords
            )

            quoteDao.insertQuoteHistory(quoteHistory)
        }
    }

    fun loadHistoryData(symbol: String): ArrayList<CandleEntry> {
        Log.i("loadChart", "repo - loadHistoryData()")

        val list = ArrayList<CandleEntry>()
        quoteDao.getHistory(symbol).let {

            it?.let {
                Log.i("loadChart", "repo symbol - ${it}")

                for ((index, r) in it.records.withIndex()) {

                    val entry = CandleEntry(
                        index.toFloat(),
                        r.high.toFloat(),
                        r.low.toFloat(),
                        r.open.toFloat(),
                        r.close.toFloat()
                    )

                    Log.i("loadChart", "entry - ${entry.x}, ${entry.high}, ${entry.low}")

                    list.add(entry)
                }
                Log.i("loadChart", "repo list size - ${list.size}")

            }

            return list

        }
    }


}