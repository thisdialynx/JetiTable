package lnx.jetitable.services.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import lnx.jetitable.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("TYPE", EventType::class.java) ?: return
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("TYPE") as? EventType ?: return
        }
        val name = intent.getStringExtra("NAME") ?: return
        val time = intent.getStringExtra("TIME") ?: return

        Log.d(RECEIVER_NAME, "Received alarm for $type: $name")

        sendNotification(type, name, time, context, pendingIntent, notificationManager)
    }

    fun sendNotification(
        type: EventType,
        name: String,
        time: String,
        context: Context,
        pendingIntent: PendingIntent,
        notificationManager: NotificationManager
    ) {
        val isClass = type == EventType.CLASS
        val channel = if (isClass) CHANNEL_CLASS_REMINDER else CHANNEL_EXAM_REMINDER
        val notificationId = if (isClass) CLASS_NOTIFICATION_ID else EXAM_NOTIFICATION_ID
        val title = if (isClass) context.getString(R.string.class_notification_title)
            else context.getString(R.string.exam_notification_title)
        val icon = if (isClass) R.drawable.ic_calendar_month else R.drawable.ic_contract_edit

        val notification = NotificationCompat.Builder(context, channel)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(context.getString(R.string.reminder_notification_description_minutes, name, time))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    companion object {
        private const val RECEIVER_NAME = "NotificationReceiver"
        private const val CHANNEL_CLASS_REMINDER = "class_reminder_channel"
        private const val CHANNEL_EXAM_REMINDER = "exam_reminder_channel"
        private const val CLASS_NOTIFICATION_ID = 3149
        private const val EXAM_NOTIFICATION_ID = 5149
    }
}