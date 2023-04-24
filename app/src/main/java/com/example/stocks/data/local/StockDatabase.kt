package com.example.stocks.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stocks.data.local.entity.StockEntity

@Database(
    entities = [StockEntity::class],
    version = 1
)
abstract class StockDatabase: RoomDatabase() {

    abstract val dao: StockDao
}