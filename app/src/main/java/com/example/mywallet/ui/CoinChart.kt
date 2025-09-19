// ui/components/CoinChart.kt
package com.example.mywallet.ui

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun CoinPriceChart(prices: List<Double>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                axisRight.isEnabled = false

                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                axisLeft.setDrawGridLines(true)
                legend.isEnabled = false
            }
        },
        update = { chart ->
            val entries = prices.mapIndexed { index, value ->
                Entry(index.toFloat(), value.toFloat())
            }

            val dataSet = LineDataSet(entries, "Price").apply {
                color = Color.GREEN
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
            }

            chart.data = LineData(dataSet)
            chart.invalidate()
        },
        modifier = modifier
            .height(250.dp)
            .fillMaxWidth()
    )
}
