package com.krystofmacek.marketviz.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.krystofmacek.marketviz.BuildConfig
import com.krystofmacek.marketviz.network.MarketDataAPI
import com.krystofmacek.marketviz.utils.IndexListGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    ).build()

    // Retrofit instance
    @Provides
    @Singleton
    fun provideMarketDataRetrofitInstance(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.MARKET_DATA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideMarketDataApiService(retrofit: Retrofit): MarketDataAPI = retrofit.create(MarketDataAPI::class.java)

    @Provides
    @Singleton
    fun provideIndexListGenerator(): IndexListGenerator = IndexListGenerator()

}