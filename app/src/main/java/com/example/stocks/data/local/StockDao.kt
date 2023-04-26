package com.example.stocks.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stocks.data.local.entity.StockEntity

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStocks(list: List<StockEntity>)

    @Query("DELETE FROM stockentity where ticker not in (:tickers)")
    suspend fun deleteStocks(tickers: List<String>)

    @Query("SELECT * FROM stockentity")
    suspend fun getStocks() : List<StockEntity>
}