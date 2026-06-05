package lnx.jetitable.misc

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import lnx.jetitable.services.data.DataSyncService
import timber.log.Timber
import javax.inject.Inject

class AndroidSyncManager @Inject constructor(
    @ApplicationContext private val context: Context
) : SyncManager {
    private val intent = Intent(context, DataSyncService::class.java)

    override fun startSync() {
        intent.also {
            context.startService(it)
            Timber.d("Sync service started")
        }
    }

    override fun stopSync() {
        intent.also {
            context.stopService(it)
            Timber.d("Sync service stopped")
        }
    }
}