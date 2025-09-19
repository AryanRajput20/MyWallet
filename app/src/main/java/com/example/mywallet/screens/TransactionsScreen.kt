package com.example.mywallet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywallet.model.Transaction
import com.example.mywallet.viewmodel.CoinTransactionViewModel

val TransactionsGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF071226), Color(0xFF0E1B2A))
)

@Composable
fun TransactionsScreen(viewModel: CoinTransactionViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("All") } // All, BUY, SELL

    val filteredTransactions = viewModel.transactionList.filter {
        (filter == "All" || it.type == filter) &&
                it.coinName.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TransactionsGradient)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text("Transactions", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            // ---------------- Search Bar ----------------
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White) },
                placeholder = { Text("Search by coin", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFF344155),
                    cursorColor = Color.White,
                    focusedLabelColor = Color(0xFF4CAF50),
                    unfocusedLabelColor = Color(0xFF9AA7B2)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---------------- Filter Tabs ----------------
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("All", "BUY", "SELL").forEach { type ->
                    Button(
                        onClick = { filter = type },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (filter == type) Color(0xFF4CAF50) else Color(0xFF1F2F45)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(type, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ---------------- Transactions List ----------------
            if (filteredTransactions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No transactions found.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredTransactions) { tx: Transaction ->
                        TransactionItem(tx, onDelete = { viewModel.transactionList.remove(tx) })
                    }
                }
            }
        }
    }
}

// ---------------- Transaction Item with Delete ----------------
@Composable
fun TransactionItem(transaction: Transaction, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2F45)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = transaction.type,
                color = if (transaction.type == "BUY") Color(0xFF4CAF50) else Color(0xFFEF5350),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("${transaction.quantity} ${transaction.coinName}", color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Text("$${"%,.2f".format(transaction.total)}", color = Color(0xFFB6C3D0))

            Spacer(modifier = Modifier.width(8.dp))
            // Delete button
            TextButton(onClick = onDelete) {
                Text("Delete", color = Color.Red)
            }
        }
    }
}














