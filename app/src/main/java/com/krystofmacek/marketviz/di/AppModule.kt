package com.krystofmacek.marketviz.di

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.krystofmacek.marketviz.BuildConfig
import com.krystofmacek.marketviz.db.QuoteDao
import com.krystofmacek.marketviz.db.QuoteDatabase
import com.krystofmacek.marketviz.network.MarketDataAPI
import com.krystofmacek.marketviz.network.MarketDataService
import com.krystofmacek.marketviz.network.SymbolAutoCompleteAPI
import com.krystofmacek.marketviz.network.SymbolAutoCompleteService
import com.krystofmacek.marketviz.repository.MarketDataRepository
import com.krystofmacek.marketviz.ui.adapters.AutoCompleteAdapter
import com.krystofmacek.marketviz.ui.adapters.MarketIndexAdapter
import com.krystofmacek.marketviz.ui.adapters.PositionAdapter
import com.krystofmacek.marketviz.ui.adapters.WatchlistQuoteAdapter
import com.krystofmacek.marketviz.utils.Constants.DB_NAME
import com.krystofmacek.marketviz.utils.IndexListGenerator
import com.krystofmacek.marketviz.utils.NetworkHelper
import com.krystofmacek.marketviz.workers.IndicesDataUpdateWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    // utils
    @Provides
    @Singleton
    fun provideNetworkHelper(@ApplicationContext app: Context): NetworkHelper = NetworkHelper(app)

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
    @Named("MarketData")
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
    @Named("AutoComplete")
    fun provideAutoCompleteRetrofitInstance(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.SYMBOL_AUTOFILL_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()



    @Provides
    @Singleton
    fun provideMarketDataApi(@Named("MarketData")retrofit: Retrofit): MarketDataAPI = retrofit.create(MarketDataAPI::class.java)

    @Provides
    @Singleton
    fun provideMarketDataService(nh: NetworkHelper, api: MarketDataAPI, generator: IndexListGenerator, @ApplicationContext app: Context) = MarketDataService(nh, api, generator, app)


    @Provides
    @Singleton
    fun provideSymbolAutoCompleteApi(@Named("AutoComplete")retrofit: Retrofit): SymbolAutoCompleteAPI = retrofit.create(SymbolAutoCompleteAPI::class.java)

    @Provides
    @Singleton
    fun provideSymbolAutoCompleteService(nh: NetworkHelper, api: SymbolAutoCompleteAPI) = SymbolAutoCompleteService(nh, api)

    // Room Database
    @Provides
    @Singleton
    fun provideQuoteDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        QuoteDatabase::class.java,
        DB_NAME
    ).addCallback(
        /** Override onCreate - Populate database when created for the first time */
        object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                WorkManager.getInstance(app)
                    .beginWith(
                        OneTimeWorkRequest
                            .Builder(IndicesDataUpdateWorker::class.java)
                            .setConstraints(
                                Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build())
                            .build()
                    )
                    .enqueue()
            }
        }
    )
        .fallbackToDestructiveMigration()
        .build()

     // DAO
    @Provides
    @Singleton
    fun provideQuoteDao(db: QuoteDatabase): QuoteDao = db.getQuoteDao()

    @Provides
    @Singleton
    fun provideRepository(
        dataService: MarketDataService,
        autocompleteService: SymbolAutoCompleteService,
        dao: QuoteDao
    ): MarketDataRepository = MarketDataRepository(dataService,autocompleteService, dao)

    // Adapters
    @Provides
    @Singleton
    fun provideMarketIndexAdapter(): MarketIndexAdapter = MarketIndexAdapter()


    @Provides
    @Singleton
    fun provideAutocompleteAdapter(): AutoCompleteAdapter = AutoCompleteAdapter()

    @Provides
    @Singleton
    fun provideWatchlistQuoteAdapter(): WatchlistQuoteAdapter = WatchlistQuoteAdapter()

    @Provides
    @Singleton
    fun providePositionAdapter(): PositionAdapter = PositionAdapter()

}