package com.krystofmacek.marketviz.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.krystofmacek.marketviz.BuildConfig
import com.krystofmacek.marketviz.db.QuoteDao
import com.krystofmacek.marketviz.db.QuoteDatabase
import com.krystofmacek.marketviz.network.MarketDataAPI
import com.krystofmacek.marketviz.network.MarketDataService
import com.krystofmacek.marketviz.utils.Constants.DB_NAME
import com.krystofmacek.marketviz.utils.IndexListGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideIndexListGenerator(): IndexListGenerator = IndexListGenerator()

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
    fun provideMarketDataApi(retrofit: Retrofit): MarketDataAPI = retrofit.create(MarketDataAPI::class.java)

    @Provides
    @Singleton
    fun provideMarketDataService(api: MarketDataAPI, generator: IndexListGenerator, @ApplicationContext app: Context) = MarketDataService(api, generator, app)


    // Room Database
    @Provides
    @Singleton
    fun provideQuoteDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        QuoteDatabase::class.java,
        DB_NAME
    ).build()

     // DAO
    @Provides
    @Singleton
    fun provideQuoteDao(db: QuoteDatabase): QuoteDao = db.getQuoteDao()

}