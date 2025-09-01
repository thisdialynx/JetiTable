package lnx.jetitable.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import lnx.jetitable.services.data.DataSyncService

class AutoStart: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return

        val serviceIntent = Intent(context, DataSyncService::class.java)
        context.startService(serviceIntent)
    }
}