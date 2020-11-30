package com.krystofmacek.marketviz.ui.app


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.krystofmacek.marketviz.R
import com.krystofmacek.marketviz.workers.IndicesDataUpdateWorker
import com.krystofmacek.marketviz.workers.WatchlistAndPortfolioDataUpdateWorker
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
        setupPeriodicWorkers(WorkManager
            .getInstance(applicationContext));
    }

    private fun setupPeriodicWorkers(workManager: WorkManager) {
        workManager.enqueueUniquePeriodicWork(
            "Indices Update",
            ExistingPeriodicWorkPolicy.REPLACE,
            buildIndicesUpdateRequest()
        )
        workManager.enqueueUniquePeriodicWork(
            "Portfolio Update",
            ExistingPeriodicWorkPolicy.REPLACE,
            buildPortfolioUpdateRequest()
        )

    }

    private fun buildIndicesUpdateRequest(): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<IndicesDataUpdateWorker>(
            repeatInterval = 12, TimeUnit.HOURS, flexTimeInterval = 1, TimeUnit.HOURS
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()
    }
    private fun buildPortfolioUpdateRequest(): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<WatchlistAndPortfolioDataUpdateWorker>(
            repeatInterval = 12, TimeUnit.HOURS, flexTimeInterval = 1, TimeUnit.HOURS
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()
    }

}