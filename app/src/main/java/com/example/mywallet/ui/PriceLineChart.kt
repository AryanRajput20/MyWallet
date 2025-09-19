package com.example.mywallet.ui.components

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun PriceLineChart(prices: List<Double>) {
    AndroidView(factory = { context ->
        LineChart(context).apply {
            val entries = prices.mapIndexed { index, price ->
                Entry(index.toFloat(), price.toFloat())
            }

            val dataSet = LineDataSet(entries, "Price History").apply {
                color = Color.BLUE
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            val lineData = LineData(dataSet)
            data = lineData

            description = Description().apply { text = "" }
            legend.isEnabled = false
            axisRight.isEnabled = false
            xAxis.setDrawLabels(false)
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            invalidate()
        }
    })
}
