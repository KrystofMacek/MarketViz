package com.krystofmacek.marketviz.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
    fun round(x: Double):Double {
        return BigDecimal(x).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

    fun getToday(): String {
        return LocalDateTime.now().format( DateTimeFormatter.ofPattern("yyyyMMdd"))
    }
}