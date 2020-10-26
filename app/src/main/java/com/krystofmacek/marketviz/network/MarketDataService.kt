package com.krystofmacek.marketviz.network

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.util.Log
import com.krystofmacek.marketviz.model.Quote
import com.krystofmacek.marketviz.model.QuoteResponse
import com.krystofmacek.marketviz.utils.Constants.DEFAULT_FIELDS
import com.krystofmacek.marketviz.utils.IndexListGenerator
import com.krystofmacek.marketviz.utils.Resource
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class MarketDataService @Inject constructor(
    val api: MarketDataAPI,
    val gen: IndexListGenerator,
    val app: Context
) {

    suspend fun loadIndices(): Resource<QuoteResponse> {
        return loadIndices(DEFAULT_FIELDS)
    }

    private suspend fun loadIndices(fields: String): Resource<QuoteResponse> {
        val indices = gen.getIndices()
        return safeApiCall { api.getQuotes(indices, fields) }

    }

    private inline fun safeApiCall(responseFunction: () -> Response<QuoteResponse>): Resource<QuoteResponse> {

        return if(checkInternetConnection()) {
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

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}