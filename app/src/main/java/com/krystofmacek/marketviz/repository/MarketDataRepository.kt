package com.krystofmacek.marketviz.repository

import com.github.mikephil.charting.data.CandleEntry
import com.krystofmacek.marketviz.db.dao.QuoteDao
import com.krystofmacek.marketviz.model.databasemodels.*
import com.krystofmacek.marketviz.model.networkmodels.autocomplete.Symbols
import com.krystofmacek.marketviz.network.MarketDataService
import com.krystofmacek.marketviz.network.SymbolAutoCompleteService
import com.krystofmacek.marketviz.utils.Constants.LONG_POSITION
import com.krystofmacek.marketviz.utils.Constants.SHORT_POSITION
import com.krystofmacek.marketviz.utils.Utils
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject


class MarketDataRepository @Inject constructor(
    private val marketDataService: MarketDataService,
    private val symbolAutoCompleteService: SymbolAutoCompleteService,
    private val quoteDao: QuoteDao
) {

    /** MARKET OVERVIEW */

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
                this.getHistory(quote.symbol)
            }
        }
    }

    /** get indices from database */
    fun getAllIndices(): Flow<List<MarketIndex>> = quoteDao.getMarketIndices()


    /** SEARCH AUTOCOMPLETE */

    /** get autocomplete data for keyword */
    suspend fun getAutoCompleteSymbolsFor(keyword: String): Symbols {
        symbolAutoCompleteService.getSymbolsFor(keyword).data?.let {
            return it
        }
        return Symbols()
    }

    /** get data for quote */
    suspend fun searchQuote(keyword: String) {

        val res = marketDataService.searchQuote(keyword)
        if(res.data == null) {
            res.message?.let {
                val searchQuoteResult = DetailsQuote(symbol = it, isEmpty = true)
                quoteDao.clearDetailsTable()
                quoteDao.insertDetailsQuote(searchQuoteResult)
            }
        } else {
            res.data.let {
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
                return
            }
        }

    }


    /** QUOTE DETAILS */

    /** get data for specified Quote */
    fun getDetailsQuote(): Flow<DetailsQuote> = quoteDao.getDetailsQuote()


    /** WATCHLIST */
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

    /** PORTFOLIO */

    /**
     * Create Long position
     * */
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

    /**
     * Create Short position
     * */
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


    /** HISTORY */

    /**
     * Get Quote History from API and save to DB
     * */
    suspend fun updatePortfolioAndWatchlistData() {
        quoteDao.getPortfolioSymbols().let {
            var quotes = ""
            for (i in it) {
                quotes += "$i,"
            }
            loadAndUpdatePortfolioData(quotes)
        }
        quoteDao.getWatchlistSymbols().let {
            var quotes = ""
            for (i in it) {
                quotes += "$i,"
            }
            loadAndUpdateWatchlistData(quotes)
        }
    }

    private suspend fun loadAndUpdatePortfolioData(quotes: String) {
        marketDataService.loadPortfolioData(quotes).data?.let { response ->
            for (quote in response.quotes) {
                quoteDao.updatePosition(quote.symbol, quote.lastPrice)
            }
        }
    }

    private suspend fun loadAndUpdateWatchlistData(quotes: String) {
        marketDataService.loadWatchlistData(quotes).data?.let { response ->
            for (quote in response.quotes) {
                quoteDao.updateWatchlistQuote(quote.symbol, quote.lastPrice, quote.netChange, quote.percentChange)
            }
        }
    }


    /**
     * Get Quote History from API and save to DB
     * */
    private suspend fun getHistory(symbol: String) {
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
            }
            val quoteHistory = QuoteHistory(
                symbol = historySymbol,
                records = historyRecords
            )
            quoteDao.insertQuoteHistory(quoteHistory)
        }
    }

    /**
     * Load Quote History from DB create and return list of CandleEntries
     * */
    fun loadHistoryData(symbol: String): ArrayList<CandleEntry> {
        val list = ArrayList<CandleEntry>()
        quoteDao.getHistory(symbol).let {
            it?.let {
                for ((index, r) in it.records.withIndex()) {

                    val entry = CandleEntry(
                        index.toFloat(),
                        r.high.toFloat(),
                        r.low.toFloat(),
                        r.open.toFloat(),
                        r.close.toFloat()
                    )
                    list.add(entry)
                }
            }
            return list
        }
    }


}