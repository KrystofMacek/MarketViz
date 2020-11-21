package com.krystofmacek.marketviz.utils

import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleDataSet
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

    fun setupCandleStickChart(chart: CandleStickChart) {
        chart.apply {
            this.isHighlightPerDragEnabled = true
            this.setDrawBorders(true)
            this.setBorderColor(Color.LTGRAY)
            this.axisLeft?.apply {
                setDrawGridLines(false)
                setDrawLabels(false)
            }
            this.axisRight?.apply {
                setDrawGridLines(true)
                setDrawLabels(true)
                textColor = Color.WHITE
            }
            this.xAxis?.apply {
                setDrawGridLines(false)
                setDrawLabels(false)
                granularity = 1f
                isGranularityEnabled = true
                setAvoidFirstLastClipping(false)
                textColor = Color.WHITE
            }
        }
    }
    fun setupCandlestickDataSet(dataSet: CandleDataSet) {
        dataSet.apply {
            color = Color.rgb(80, 80, 80)
            shadowColor = Color.GRAY
            shadowWidth = 0.8f

            decreasingColor = Color.RED
            decreasingPaintStyle = Paint.Style.FILL

            increasingColor = Color.GREEN
            increasingPaintStyle = Paint.Style.FILL

            neutralColor = Color.GRAY

            setDrawValues(false)
        }
    }
}