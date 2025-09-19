package com.example.mywallet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywallet.viewmodel.CoinTransactionViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreen(
    auth: FirebaseAuth,
    viewModel: CoinTransactionViewModel,
    onLogout: () -> Unit
) {
    val user = auth.currentUser
    var isDarkTheme by remember { mutableStateOf(true) } // Theme state

    // Compute portfolio summary
    val transactions = viewModel.transactionList
    val coinMap = viewModel.coinList.associateBy { it.id }

    val totalInvested = transactions.sumOf { if (it.type == "BUY") it.total else -it.total }
    val currentValue = transactions.sumOf {
        val coinPrice = coinMap[it.coinId]?.price ?: it.price
        if (it.type == "BUY") coinPrice * it.quantity else -coinPrice * it.quantity
    }
    val profitLoss = currentValue - totalInvested
    val coinCount = transactions.groupBy { it.coinId }.size

    val backgroundColor = if (isDarkTheme) Color(0xFF1E1E2C) else Color(0xFFF5F5F5)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val cardColor = if (isDarkTheme) Color(0xFF1F2F45) else Color(0xFFE0E0E0)
    val profitColor = if (profitLoss >= 0) Color(0xFF4CAF50) else Color(0xFFEF5350)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(top = 32.dp)
        ) {
            Text("Profile", color = textColor, fontSize = 26.sp)

            Spacer(Modifier.height(24.dp))

            // Profile Picture
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (user?.displayName?.firstOrNull()?.uppercaseChar() ?: 'U').toString(),
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            // Theme Toggle Button
            Button(
                onClick = { isDarkTheme = !isDarkTheme },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkTheme) Color(0xFF4CAF50) else Color(0xFF1F2F45)
                )
            ) {
                Text(
                    text = if (isDarkTheme) "Switch to Light Theme" else "Switch to Dark Theme",
                    color = Color.White
                )
            }

            Spacer(Modifier.height(16.dp))

            Text("Name: ${user?.displayName ?: "N/A"}", color = textColor)
            Text("Email: ${user?.email ?: "N/A"}", color = textColor)

            Spacer(Modifier.height(32.dp))

            // Portfolio Summary
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Portfolio Summary", color = textColor, fontSize = 18.sp)
                    Spacer(Modifier.height(12.dp))
                    Text("Coins Owned: $coinCount", color = textColor)
                    Text("Total Invested: $${"%,.2f".format(totalInvested)}", color = textColor)
                    Text("Current Value: $${"%,.2f".format(currentValue)}", color = textColor)
                    Text(
                        "Profit / Loss: $${"%,.2f".format(profitLoss)}",
                        color = profitColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // Logout Button
            Button(
                onClick = {
                    auth.signOut()
                    onLogout()
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Logout", color = Color.White)
            }
        }
    }
}









