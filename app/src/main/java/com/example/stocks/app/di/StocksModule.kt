package com.example.stocks.app.di

import android.app.Application
import androidx.room.Room
import com.example.stocks.data.local.StockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StocksModule {
    @Provides
    @Singleton
    fun provideStockDatabase(app: Application): StockDatabase {
        return Room.databaseBuilder(app, StockDatabase::class.java, "stock_db").build()
    }
}