package com.krystofmacek.marketviz.utils

import java.time.LocalDate

class IndexListGenerator {

    private val SP = "ES"
    private val NASDAQ = "NQ"
    private val DOWJ = "YM"
    private val RUSS = "QR"

    private val indices = listOf(SP, NASDAQ, DOWJ, RUSS)

    private fun generateIndices(): String {
        val month = LocalDate.now().monthValue
        val year = LocalDate.now().year - 2000

        val quarter = when(month) {
            1,2,3 -> "H"
            4,5,6 -> "M"
            7,8,9 -> "U"
            else -> "Z"
        }

        var indicesQuery = ""
        for(i in indices) {
            indicesQuery += "$i$quarter$year,"
        }

        return indicesQuery
    }

    fun getIndices():String {
        return generateIndices()
    }

}