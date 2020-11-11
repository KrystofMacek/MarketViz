package com.krystofmacek.marketviz.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.krystofmacek.marketviz.model.databasemodels.HistoryRecord

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun historyRecordToString(list: List<HistoryRecord>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun historyRecordFromString(list: String): List<HistoryRecord> {
        val type = object : TypeToken<List<HistoryRecord>>() {}.type
        return gson.fromJson(list, type)
    }

}