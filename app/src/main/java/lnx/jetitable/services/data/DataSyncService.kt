package lnx.jetitable.services.data

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataSyncService @Inject constructor() : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scheduleDataSync()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    fun scheduleDataSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<UserDataWorker>(6, TimeUnit.HOURS)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .addTag(DATA_SYNC_SERVICE_NAME)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            DATA_SYNC_SERVICE_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            syncRequest
        )
    }
    companion object {
        const val DATA_SYNC_SERVICE_NAME = "data_sync_service"
    }
}