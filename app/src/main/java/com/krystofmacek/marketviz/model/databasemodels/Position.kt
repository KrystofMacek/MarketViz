package com.krystofmacek.marketviz.model.databasemodels

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Model to be used when saving and displaying positions in portfolio */

@Entity(tableName = "positions_table")
class Position (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val entryPrice: Double,
    val lastPrice: Double,
    val size: Int,
    val symbol: String,
    val name: String,
    val positionType: Int
)