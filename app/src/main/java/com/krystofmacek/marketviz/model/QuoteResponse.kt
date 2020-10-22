package com.krystofmacek.marketviz.model

data class QuoteResponse(
    val quotes: List<Quote>,
    val status: String
)
