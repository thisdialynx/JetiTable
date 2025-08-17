package lnx.jetitable.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import lnx.jetitable.services.data.DataSyncService

class AutoStart: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED && context != null) {
            Intent(context, DataSyncService::class.java).also {
                context.startService(it)
            }
        }
    }
}