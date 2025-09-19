package com.example.mywallet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.mywallet.navigation.Screen
import com.example.mywallet.viewmodel.CoinTransactionViewModel

@Composable
fun HomeScreen(
    bottomNavController: NavHostController,
    appNavController: NavHostController,
    viewModel: CoinTransactionViewModel
) {
    val coins = viewModel.coinList

    LaunchedEffect(Unit) {
        viewModel.fetchCoins()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        if (coins.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF4CAF50)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                items(coins) { coin ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                appNavController.navigate(
                                    Screen.CoinDetail.createRoute(coin.id, coin.name, coin.price)
                                )
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = coin.image,
                                contentDescription = coin.name,
                                modifier = Modifier.size(48.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(coin.name, color = Color.White)
                                Text(
                                    "$${"%,.2f".format(coin.price)}",
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "${"%.2f".format(coin.change24h)}%",
                                color = if (coin.change24h >= 0) Color(0xFF4CAF50) else Color(0xFFEF5350)
                            )
                        }
                    }
                }
            }
        }
    }
}














