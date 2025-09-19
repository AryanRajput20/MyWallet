package com.example.mywallet.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.mywallet.model.Coin
import com.example.mywallet.model.Transaction
import java.util.UUID

class WalletViewModel : ViewModel() {

    // Sample coins
    val coins = mutableStateListOf(
        Coin(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC",
            price = 30000.0,
            change24h = 2.5,
            image = "https://crypto.com/btc.png"
        ),
        Coin(
            id = "ETH",
            name = "Ethereum",
            symbol = "ETH",
            price = 2000.0,
            change24h = -1.2,
            image = "https://crypto.com/eth.png"
        ),
        Coin(
            id = "ADA",
            name = "Cardano",
            symbol = "ADA",
            price = 1.5,
            change24h = 0.8,
            image = "https://crypto.com/ada.png"
        )
    )

    val transactions = mutableStateListOf<Transaction>()

    // Buy Coin
    fun buyCoin(coin: Coin, quantity: Double) {
        val tx = Transaction(
            id = UUID.randomUUID().toString(),
            coinId = coin.id,
            coinName = coin.name,
            type = "BUY",
            quantity = quantity,
            price = coin.price,
            total = coin.price * quantity
        )
        transactions.add(tx)
    }

    // Sell Coin
    fun sellCoin(coin: Coin, quantity: Double) {
        val tx = Transaction(
            id = UUID.randomUUID().toString(),
            coinId = coin.id,
            coinName = coin.name,
            type = "SELL",
            quantity = quantity,
            price = coin.price,
            total = coin.price * quantity
        )
        transactions.add(tx)
    }

    // Current Portfolio
    fun getPortfolio(): Map<String, Double> {
        val portfolio = mutableMapOf<String, Double>()
        for (tx in transactions) {
            val current = portfolio[tx.coinId] ?: 0.0
            portfolio[tx.coinId] =
                if (tx.type == "BUY") current + tx.quantity else current - tx.quantity
        }
        return portfolio
    }
}


