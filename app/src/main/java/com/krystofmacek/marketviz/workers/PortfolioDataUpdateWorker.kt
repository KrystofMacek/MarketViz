package com.krystofmacek.marketviz.workers

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.krystofmacek.marketviz.repository.MarketDataRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class PortfolioDataUpdateWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val repository: MarketDataRepository
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {

        try {
            val jobs = async {
                repository.updatePortfolioData()
            }
            jobs.await()
            Result.success()
        } catch (e: Throwable) {
            Result.failure()
        }
    }

}