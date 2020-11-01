package com.krystofmacek.marketviz.utils

import java.math.BigDecimal
import java.math.RoundingMode

object Utils {
    fun round(x: Double):Double {
        return BigDecimal(x).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }
}