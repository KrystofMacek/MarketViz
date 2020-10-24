package com.krystofmacek.marketviz.ui.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * @HiltAndroidApp kicks off the code generation of the Hilt components and
 *  also generates a base class for your application that uses those generated components.
 * */
@HiltAndroidApp
class App: Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder().setWorkerFactory(workerFactory).build()

}