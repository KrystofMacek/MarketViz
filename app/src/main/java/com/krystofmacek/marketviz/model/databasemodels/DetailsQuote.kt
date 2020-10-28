package com.krystofmacek.marketviz.model.databasemodels

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "details_quote_table")
class DetailsQuote (
    @PrimaryKey
    val symbol: String,
    val name: String,
    val lastPrice: Double,
    val netChange: Double,
    val percentageChange: Double
): Serializable