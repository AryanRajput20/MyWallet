package com.example.mywallet.network

import com.example.mywallet.model.CoinResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApi {

    // 1. Get list of top coins
    @GET("coins/markets?vs_currency=usd&per_page=20&page=1&sparkline=false")
    suspend fun getCoins(): List<CoinResponse>

    // 2. Get historical prices for graph
    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") coinId: String,
        @Query("vs_currency") currency: String = "usd",
        @Query("days") days: Int = 7
    ): MarketChartResponse
}

// Response model for chart
data class MarketChartResponse(
    val prices: List<List<Double>> // [[timestamp, price], ...]
)


