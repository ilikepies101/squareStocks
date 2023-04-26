package com.example.stocks.app.di

import com.example.stocks.data.StockApi
import com.example.stocks.data.local.StockDatabase
import com.example.stocks.domain.repository.StockRepository
import com.example.stocks.domain.repository.StockRepositoryImpl
import com.example.stocks.domain.use_case.GetStocks
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @ViewModelScoped
    @Provides
    fun provideGetStocksUseCase(repository: StockRepository) = GetStocks(repository)

    @Provides
    @ViewModelScoped
    fun provideStockRepository(api: StockApi, db: StockDatabase): StockRepository {
        return StockRepositoryImpl(api, db.dao)
    }

    @Provides
    @ViewModelScoped
    fun provideStockApi(): StockApi {
        return Retrofit.Builder().addConverterFactory(
            GsonConverterFactory.create()
        ).baseUrl(StockApi.BASE_URL).build().create(StockApi::class.java)
    }
}