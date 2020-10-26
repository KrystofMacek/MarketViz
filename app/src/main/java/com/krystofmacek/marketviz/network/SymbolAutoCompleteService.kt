package com.krystofmacek.marketviz.network

import com.krystofmacek.marketviz.model.autocomplete.Symbols
import com.krystofmacek.marketviz.model.marketdata.QuoteResponse
import com.krystofmacek.marketviz.utils.NetworkHelper
import com.krystofmacek.marketviz.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class SymbolAutoCompleteService @Inject constructor(
    val networkHelper: NetworkHelper,
    val api: SymbolAutoCompleteAPI
) {



    suspend fun getSymbolsFor(keyword: String): Resource<Symbols> {
        return safeApiCall { api.getAutoCompleteSymbols(keyword) }
    }


    private inline fun safeApiCall(responseFunction: () -> Response<Symbols>): Resource<Symbols> {

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

    private fun handleResult(response: Response<Symbols>): Resource<Symbols> {
        if(response.isSuccessful) {
            response.body()?.let {
                return Resource.success(it)
            }
        }
        return Resource.error(message = response.message())
    }

}