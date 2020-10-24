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
import java.time.Duration

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Setup bottom navigation */
        val navigationController = nav_host_fragment.findNavController()
        bottomNavigationView.setupWithNavController(navigationController)

        /** Setup Work Manager */
        WorkManager
            .getInstance(applicationContext)
            .enqueue(buildWorkRequests())

    }

    private fun buildWorkRequests(): List<WorkRequest> {
        val indicesUpdateRequest =
            PeriodicWorkRequestBuilder<IndicesDataUpdateWorker>(
                repeatInterval = Duration.ofHours(2),
                flexTimeInterval = Duration.ofMinutes(10)
            ).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).build()

        return listOf(indicesUpdateRequest)
    }

}