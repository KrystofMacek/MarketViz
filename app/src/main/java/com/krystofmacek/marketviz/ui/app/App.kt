package com.krystofmacek.marketviz.ui.app

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.krystofmacek.marketviz.workers.IndicesDataUpdateWorker
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

    override fun onCreate() {
        super.onCreate()

        /** on app create - run update on saved data */
        Log.i("woorkmanager", " Oncreate APP")
        WorkManager.getInstance(this)
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