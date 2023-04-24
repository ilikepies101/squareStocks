package com.example.stocks.app.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stocks.data.StockApi
import com.example.stocks.data.local.StockDao
import com.example.stocks.data.local.StockDatabase
import com.example.stocks.domain.repository.StockRepository
import com.example.stocks.domain.repository.StockRepositoryImpl
import com.example.stocks.domain.use_case.GetStocks
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StocksModule {

    @Singleton
    @Provides
    fun provideGetStocksUseCase(repository: StockRepository) = GetStocks(repository)

    @Provides
    @Singleton
    fun provideStockRepository(api: StockApi, db: StockDatabase): StockRepository {
        return StockRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideStockDatabase(app: Application): StockDatabase {
        return Room.databaseBuilder(app, StockDatabase::class.java, "stock_db").build()
    }

    @Provides
    @Singleton
    fun provideStockApi(): StockApi {
        return Retrofit.Builder().addConverterFactory(
            GsonConverterFactory.create()
        ).baseUrl(StockApi.BASE_URL).build().create(StockApi::class.java)
    }
}