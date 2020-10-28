package com.krystofmacek.marketviz.network


import com.krystofmacek.marketviz.BuildConfig
import com.krystofmacek.marketviz.model.networkmodels.autocomplete.Symbols
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface SymbolAutoCompleteAPI {

    @GET("${BuildConfig.SYMBOL_AUTOFILL_BASE_URL}/{keyword}")
    suspend fun getAutoCompleteSymbols(
        @Path("keyword") keyword: String
    ) : Response<Symbols>

}