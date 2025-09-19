package com.example.mywallet.model

import androidx.compose.runtime.mutableStateListOf

object TransactionManager {
    private val _transactions = mutableStateListOf<Transaction>()
    val transactions: List<Transaction> get() = _transactions

    fun addTransaction(transaction: Transaction) {
        _transactions.add(transaction)
    }
}
