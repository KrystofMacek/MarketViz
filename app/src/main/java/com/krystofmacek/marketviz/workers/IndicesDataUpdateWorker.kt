package com.krystofmacek.marketviz.workers

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.krystofmacek.marketviz.repository.MarketDataRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.*

class IndicesDataUpdateWorker @WorkerInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val repository: MarketDataRepository
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {

        try {
            val jobs = async {
                repository.loadIndices()
                Log.i("WORK MANAGER", "INDICES LOADED at @${SimpleDateFormat().format(Date(System.currentTimeMillis()))}")
            }
            jobs.await()
            Result.success()
        } catch (e: Throwable) {
            Result.failure()
        }
    }

}