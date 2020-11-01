package com.krystofmacek.marketviz.ui.app


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.workers.IndicesDataUpdateWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Setup bottom navigation */
        val navigationController = nav_host_fragment.findNavController()
        bottomNavigationView.setupWithNavController(navigationController)

        /** Setup Periodic Work Manager Requests*/
        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "Indices Update",
                ExistingPeriodicWorkPolicy.REPLACE,
                buildIndicesUpdateRequest()
            )
    }

    private fun buildIndicesUpdateRequest(): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<IndicesDataUpdateWorker>(
                repeatInterval = 2, TimeUnit.HOURS, flexTimeInterval = 10, TimeUnit.MINUTES
            ).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).build()
    }

}