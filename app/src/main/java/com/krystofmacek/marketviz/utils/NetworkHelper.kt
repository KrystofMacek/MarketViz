package com.krystofmacek.marketviz.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


/**
 * INTERNET CONNECTIVITY HELPER CLASS
 * */
class NetworkHelper(private val app: Context) {

    fun checkInternetConnection(): Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}