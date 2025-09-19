package com.example.mywallet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.mywallet.model.Coin
import com.example.mywallet.viewmodel.CoinTransactionViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch

@Composable
fun CoinDetailScreen(
    navController: NavHostController,
    coinId: String,
    coinName: String,
    price: Double,
    viewModel: CoinTransactionViewModel
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val coinFromVm: Coin? = viewModel.coinList.find { it.id == coinId }
    val coin = coinFromVm ?: Coin(
        id = coinId,
        name = coinName,
        symbol = coinName.take(4),
        price = price,
        change24h = 0.0,
        image = ""
    )

    val backgroundBrush = Brush.verticalGradient(listOf(Color(0xFF071226), Color(0xFF0E1B2A)))

    var selectedHistory by remember { mutableStateOf(7) } // 7-day default
    val prices by remember { derivedStateOf {
        viewModel.priceHistoryMap[coinId]?.takeLast(selectedHistory) ?: listOf()
    }}

    var quantityText by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }

    // Quantity -> Amount
    fun onQuantityChanged(qText: String) {
        quantityText = qText
        val q = qText.toDoubleOrNull() ?: 0.0
        amountText = if (q > 0.0) "%.2f".format(q * coin.price) else ""
    }

    // Amount -> Quantity
    fun onAmountChanged(aText: String) {
        amountText = aText
        val a = aText.toDoubleOrNull() ?: 0.0
        quantityText = if (a > 0.0 && coin.price > 0.0) "%.6f".format(a / coin.price) else ""
    }

    // Start live price simulation
    LaunchedEffect(Unit) { viewModel.startPriceSimulation() }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(padding)
                .padding(16.dp)
        ) {
            // ---------- Coin Info ----------
            Text(coin.name, color = Color.White, fontSize = 24.sp)
            Text("Current: $${"%,.2f".format(coin.price)}", color = Color(0xFF9AA7B2))
            Spacer(Modifier.height(12.dp))

            // ---------- History Toggle ----------
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(7, 14, 30).forEach { days ->
                    Button(
                        onClick = { selectedHistory = days },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedHistory == days) Color(0xFF4CAF50) else Color(0xFF1F2F45)
                        )
                    ) { Text("$days Days", color = Color.White) }
                }
            }

            Spacer(Modifier.height(12.dp))

            // ---------- Line Chart ----------
            LineChartViewDynamic(prices = prices)

            Spacer(Modifier.height(16.dp))

            // ---------- Buy/Sell Section ----------
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1724)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Buy / Sell", color = Color(0xFFB6C3D0), fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { onQuantityChanged(it) },
                        label = { Text("Quantity", color = Color(0xFFB6C3D0)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color(0xFF344155),
                            cursorColor = Color.White,
                            focusedLabelColor = Color(0xFF4CAF50),
                            unfocusedLabelColor = Color(0xFF9AA7B2)
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { onAmountChanged(it) },
                        label = { Text("Amount (USD)", color = Color(0xFFB6C3D0)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color(0xFF344155),
                            cursorColor = Color.White,
                            focusedLabelColor = Color(0xFF4CAF50),
                            unfocusedLabelColor = Color(0xFF9AA7B2)
                        )
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                val q = quantityText.toDoubleOrNull() ?: 0.0
                                if (q > 0.0) {
                                    viewModel.addTransaction(coinId, coin.name, "BUY", q, coin.price)
                                    quantityText = ""
                                    amountText = ""
                                    scope.launch { snackbarHostState.showSnackbar("Bought $q ${coin.symbol}") }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1F8A5A))
                        ) { Text("Buy", color = Color.White) }

                        Button(
                            onClick = {
                                val q = quantityText.toDoubleOrNull() ?: 0.0
                                if (q > 0.0) {
                                    viewModel.addTransaction(coinId, coin.name, "SELL", q, coin.price)
                                    quantityText = ""
                                    amountText = ""
                                    scope.launch { snackbarHostState.showSnackbar("Sold $q ${coin.symbol}") }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB33A3A))
                        ) { Text("Sell", color = Color.White) }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ---------- Live News ----------
            Text("Latest News", color = Color.White, fontSize = 18.sp)
            val dummyNews = listOf(
                "Bitcoin hits new high!",
                "Ethereum shows bullish trend.",
                "Altcoins gaining momentum.",
                "Investors are optimistic."
            )
            dummyNews.forEach { news ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1724)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(news, color = Color.White, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun LineChartViewDynamic(prices: List<Float>, modifier: Modifier = Modifier) {
    AndroidView(factory = { context ->
        LineChart(context).apply {
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = android.graphics.Color.WHITE
            axisLeft.textColor = android.graphics.Color.WHITE
            axisRight.isEnabled = false
        }
    }, modifier = modifier.height(220.dp).fillMaxWidth()) { chart ->
        if (prices.isNotEmpty()) {
            val entries = prices.mapIndexed { index, price -> Entry(index.toFloat(), price) }
            val dataSet = LineDataSet(entries, "Price").apply {
                color = android.graphics.Color.WHITE
                setDrawFilled(true)
                fillColor = android.graphics.Color.WHITE
                fillAlpha = 50
                lineWidth = 2f
                setDrawCircles(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    }
}












