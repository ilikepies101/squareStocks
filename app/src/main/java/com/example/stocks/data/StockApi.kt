package com.example.stocks.data

import com.example.stocks.data.remote.dto.StockDto
import com.example.stocks.data.remote.dto.StockListDto
import retrofit2.http.GET

interface StockApi {

    @GET("portfolio.json")
    suspend fun getStocks(): StockListDto

    companion object {
        const val BASE_URL = "https://storage.googleapis.com/cash-homework/cash-stocks-api/"
    }
}