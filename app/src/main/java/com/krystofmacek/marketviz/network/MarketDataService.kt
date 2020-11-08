package com.krystofmacek.marketviz.network

import android.content.Context
import com.krystofmacek.marketviz.model.networkmodels.marketdata.Quote
import com.krystofmacek.marketviz.model.networkmodels.marketdata.QuoteResponse
import com.krystofmacek.marketviz.utils.Constants.DEFAULT_FIELDS
import com.krystofmacek.marketviz.utils.IndexListGenerator
import com.krystofmacek.marketviz.utils.NetworkHelper
import com.krystofmacek.marketviz.utils.Resource
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
        return safeApiCall { api.getQuotes(indices, fields) }

    }

    suspend fun searchQuote(quote: String): Resource<QuoteResponse> {
        return safeApiCall { api.getQuotes(quote, DEFAULT_FIELDS) }
    }

    /** methods to update data for potrfolio */
    suspend fun loadPortfolioData(quotes: String): Resource<QuoteResponse> {
        return safeApiCall { api.getQuotes(quotes, DEFAULT_FIELDS) }
    }

    private inline fun safeApiCall(responseFunction: () -> Response<QuoteResponse>): Resource<QuoteResponse> {

        return if(networkHelper.checkInternetConnection()) {
            try {
                handleResult(responseFunction.invoke())
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.error(e.message ?: "Unknown error")
            }
        } else {
            Resource.error(message = "No Internet Connection")
        }
    }

    private fun handleResult(response: Response<QuoteResponse>): Resource<QuoteResponse> {
        if(response.isSuccessful) {
            response.body()?.let {
                return Resource.success(it)
            }
        }
        return Resource.error(message = response.message())
    }

}