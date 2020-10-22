package com.krystofmacek.marketviz.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "quotes"
)
data class Quote(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    val symbol: String,
    val timestamp: String,
    val tradingDay: String,
    val open: Float,
    val close: Float,
    val high: Float,
    val low: Float,
    val volume: Int,
    val categories: MutableSet<Int>
)