package lnx.jetitable.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import lnx.jetitable.services.data.DataSyncService
import timber.log.Timber

class AutoStart: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return

        Timber.d("Received sync service start request")

        val serviceIntent = Intent(context, DataSyncService::class.java)
        context.startService(serviceIntent)
    }
}