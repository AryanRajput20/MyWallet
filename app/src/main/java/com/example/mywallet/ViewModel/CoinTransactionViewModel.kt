package com.example.mywallet.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywallet.model.Coin
import com.example.mywallet.model.CoinResponse
import com.example.mywallet.model.Transaction
import com.example.mywallet.network.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class CoinTransactionViewModel : ViewModel() {

    val coinList = mutableStateListOf<Coin>()
    val transactionList = mutableStateListOf<Transaction>()

    // Real-time price history per coin
    val priceHistoryMap = mutableStateMapOf<String, MutableList<Float>>()

    // ------------------ Fetch coins from API ------------------
    fun fetchCoins() {
        viewModelScope.launch {
            try {
                val response: List<CoinResponse> = RetrofitInstance.api.getCoins()
                val mappedCoins = response.map {
                    Coin(
                        id = it.id,
                        name = it.name,
                        symbol = it.symbol,
                        price = it.currentPrice,
                        change24h = it.priceChange,
                        image = it.image
                    )
                }

                coinList.clear()
                coinList.addAll(mappedCoins)

                // Initialize price history for chart
                mappedCoins.forEach { coin ->
                    val list = priceHistoryMap.getOrPut(coin.id) { mutableListOf() }
                    if (list.isEmpty()) list.add(coin.price.toFloat())
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ------------------ Add a transaction ------------------
    fun addTransaction(
        coinId: String,
        coinName: String,
        type: String, // "BUY" or "SELL"
        quantity: Double,
        price: Double
    ) {
        val transaction = Transaction(
            coinId = coinId,
            coinName = coinName,
            type = type,
            quantity = quantity,
            price = price,
            total = quantity * price
        )
        transactionList.add(transaction)

        // Update chart price
        updatePrice(coinId, price)
    }

    // ------------------ Update price for chart ------------------
    private fun updatePrice(coinId: String, newPrice: Double) {
        val list = priceHistoryMap.getOrPut(coinId) { mutableListOf() }
        if (list.size >= 20) list.removeAt(0) // keep last 20 prices
        list.add(newPrice.toFloat())
    }

    fun getTransactionsForCoin(coinId: String): List<Transaction> {
        return transactionList.filter { it.coinId == coinId }
    }

    // ------------------ Optional: simulate live price changes ------------------
    fun startPriceSimulation() {
        viewModelScope.launch {
            while (true) {
                coinList.forEach { coin ->
                    val currentPrice = coin.price
                    // random change ±1%
                    val newPrice = currentPrice * (1 + Random.nextDouble(-0.01, 0.01))
                    coin.price = newPrice
                    updatePrice(coin.id, newPrice)
                }
                delay(2000) // update every 2 seconds
            }
        }
    }
}









