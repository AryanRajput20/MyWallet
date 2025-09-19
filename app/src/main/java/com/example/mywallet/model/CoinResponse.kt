package com.example.mywallet.model

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    val id: String,
    val symbol: String,
    val name: String,
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("price_change_percentage_24h")
    val priceChange: Double,
    val image: String
)
