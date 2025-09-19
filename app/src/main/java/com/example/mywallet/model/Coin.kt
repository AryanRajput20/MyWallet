package com.example.mywallet.model

import java.util.UUID

data class Coin(
    val id: String,
    val name: String,
    val symbol: String,
    var price: Double,
    val change24h: Double,
    val image: String,
)

// ---------------------- Transaction ----------------------
data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val coinId: String,
    val coinName: String,
    val type: String, // "BUY" or "SELL"
    val quantity: Double,
    val price: Double,
    val total: Double,
    val timestamp: Long = System.currentTimeMillis()
)
