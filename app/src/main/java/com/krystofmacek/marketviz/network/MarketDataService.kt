package com.krystofmacek.marketviz.network

import android.content.Context
import com.krystofmacek.marketviz.model.networkmodels.marketdata.HistoryResponse
import com.krystofmacek.marketviz.model.networkmodels.marketdata.QuoteResponse
import com.krystofmacek.marketviz.utils.*
import com.krystofmacek.marketviz.utils.Constants.DEFAULT_FIELDS
import com.krystofmacek.marketviz.utils.Constants.HISTORY_START_DATE
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class MarketDataService @Inject constructor(
    val networkHelper: NetworkHelper,
    val api: MarketDataAPI,
    val gen: IndexListGenerator,
    val app: Context
) {

    /** Methods for market indices */
    suspend fun loadIndices(): Resource<QuoteResponse> {
        return loadIndices(DEFAULT_FIELDS)
    }

    private suspend fun loadIndices(fields: String): Resource<QuoteResponse> {
        val indices = gen.getIndices()
        return safeGetQuote { api.getQuotes(indices, fields) }

    }

    suspend fun searchQuote(quote: String): Resource<QuoteResponse> {
        return safeGetQuote { api.getQuotes(quote, DEFAULT_FIELDS) }
    }

    /** methods to update data for potrfolio */
    suspend fun loadPortfolioData(quotes: String): Resource<QuoteResponse> {
        return safeGetQuote { api.getQuotes(quotes, DEFAULT_FIELDS) }
    }

    /** Get Historical data */
    suspend fun getHistory(symbol: String): Resource<HistoryResponse> {
        return safeGetHistory { api.getHistory(
            symbol = symbol,
            type = Constants.historyType.daily,
            startDate = HISTORY_START_DATE,
            endDate = Utils.getToday(),
            order = Constants.historyOrder.asc
        ) }
    }

    /** Handle get history requests and resp */
    private inline fun safeGetHistory(responseFunction: ()  -> Response<HistoryResponse>): Resource<HistoryResponse> {

        return if(networkHelper.checkInternetConnection()) {
            try {
                handleHistoryResponse(responseFunction.invoke())
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.error(e.message ?: "Unknown error")
            }
        } else {
            Resource.error(message = "No Internet Connection")
        }
    }

    private fun handleHistoryResponse(response: Response<HistoryResponse>): Resource<HistoryResponse> {
        if(response.isSuccessful) {
            response.body()?.let {
                return Resource.success(it)
            }
        }
        return Resource.error(message = response.message())
    }


    /** Handle get quote requests and resp */
    private inline fun safeGetQuote(responseFunction: ()  -> Response<QuoteResponse>): Resource<QuoteResponse> {

        return if(networkHelper.checkInternetConnection()) {
            try {
                handleQuoteResponse(responseFunction.invoke())
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.error(e.message ?: "Unknown error")
            }
        } else {
            Resource.error(message = "No Internet Connection")
        }
    }

    private fun handleQuoteResponse(response: Response<QuoteResponse>): Resource<QuoteResponse> {
        if(response.isSuccessful) {
            response.body()?.let {
                return Resource.success(it)
            }
        }
        return Resource.error(message = response.message())
    }

}