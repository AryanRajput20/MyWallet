package com.example.mywallet.network

import com.example.mywallet.model.Coin
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=false")
    suspend fun getCoins(): Response<List<Coin>>
}

