package com.krystofmacek.marketviz.utils

object Constants {
    const val DB_NAME = "quote_db"

    const val DEFAULT_FIELDS = "fiftyTwoWkHigh,fiftyTwoWkHighDate,fiftyTwoWkLow,fiftyTwoWkLowDate,avgVolume,previousClose"

    // network
    const val MARKET_INDEX = 1
    const val SEARCH_RESULT = 2


    // source dest
    const val SEARCH_FRAGMENT = "search"
    const val WATCHLIST_FRAGMENT = "watchlist"

    const val SHORT_POSITION = 0
    const val LONG_POSITION = 1

    enum class historyType {
        ticks, minutes, nearbyMinutes, formTMinutes, daily, dailyNearest, dailyContinue, weekly, weeklyNearest, weeklyContinue, monthly, monthlyNearest, monthlyContinue, quarterly, quarterlyNearest, quarterlyContinue, yearly, yearlyNearest, yearlyContinue
    }
    enum class historyOrder {
        asc, desc
    }

    // date of first record for history -  2018 01 01
    const val HISTORY_START_DATE = "20180101"


}