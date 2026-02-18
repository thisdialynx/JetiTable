package lnx.jetitable.services.data

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataSyncService @Inject constructor() : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scheduleDataSync()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    fun scheduleDataSync() {
        val syncRequest = PeriodicWorkRequestBuilder<UserDataWorker>(6, TimeUnit.HOURS)
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