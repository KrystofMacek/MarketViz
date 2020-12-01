package com.krystofmacek.marketviz.utils


/**
 * WRAPPER CLASS FOR API RESPONSES
 * */

data class Resource<out T> (
    val status: Constants.Status,
    val data: T?,
    val message: String?
){
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(
                status = Constants.Status.SUCCESS,
                data = data,
                message = null
            )

        fun <T> error(message: String): Resource<T> =
            Resource(
                status = Constants.Status.ERROR,
                data = null,
                message = message
            )

        fun <T> loading(data: T?): Resource<T> =
            Resource(
                status = Constants.Status.LOADING,
                data = data,
                message = null
            )
    }


}