package lnx.jetitable.services.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import lnx.jetitable.R
import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.datastore.ScheduleDataStore
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@Serializable
enum class EventType { CLASS, EXAM }

class NotifManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appPrefs: AppPreferences,
    private val scheduleDataStore: ScheduleDataStore
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
            createNotificationChannels()
        }
    }

    @SuppressLint("InlinedApi")
    suspend fun updateNotificationSchedules() {
        val notifPermission =
            (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED)
        val exactAlarmPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else true
        val isPermissionsGranted = notifPermission && exactAlarmPermission
        val isNotifsEnabled = appPrefs.getNotificationPreference().first()

        if (!isPermissionsGranted) {
            appPrefs.saveNotificationPreference(false)
            Timber.d("Missing permissions, disabling notifications... $isNotifsEnabled, $exactAlarmPermission, $notifPermission")
            return
        }

        if (isNotifsEnabled) {
            val classPrefs = appPrefs.getClassPreferences().first()
            val classList = scheduleDataStore.getClassList().first()
            val examPrefs = appPrefs.getExamPreferences().first()
            val examList = scheduleDataStore.getExamList().first()

            classList.forEach { classData ->
                if (classPrefs.isEnabled) {
                    scheduleNotification(
                        classData.date,
                        classData.start,
                        EventType.CLASS,
                        classData.name,
                        classPrefs.minutes
                    )
                } else {
                    cancelNotification(
                        classData.date,
                        classData.start,
                        EventType.CLASS,
                        classData.name,
                        classPrefs.minutes
                    )
                }
            }

            examList.forEach { examData ->
                if (examPrefs.isEnabled) {
                    scheduleNotification(
                        examData.date,
                        examData.time,
                        EventType.EXAM,
                        examData.name,
                        examPrefs.minutes
                    )
                } else {
                    cancelNotification(
                        examData.date,
                        examData.time,
                        EventType.EXAM,
                        examData.name,
                        examPrefs.minutes
                    )
                }
            }
        }
    }

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val classChannel = NotificationChannel(
                CHANNEL_CLASS_REMINDER,
                context.getString(R.string.class_reminders_channel),
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = context.getString(R.string.class_reminders_channel_description) }
            val examChannel = NotificationChannel(
                CHANNEL_EXAM_REMINDER,
                context.getString(R.string.exam_reminders_channel),
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = context.getString(R.string.exam_reminders_channel_description) }

            notificationManager.createNotificationChannels(
                listOf(classChannel, examChannel)
            )
        }
    }

    private fun scheduleNotification(
        date: String,
        time: String,
        type: EventType,
        name: String,
        reminderMinutes: Int
    ) {
        try {
            val pendingIntent = getPendingIntent(date, time, type, name, reminderMinutes) ?: return

            scheduleAlarm(pendingIntent.second.timeInMillis, pendingIntent.first)
        } catch (e: Exception) {
            Timber.e(e, "Error scheduling $type notification")
        }
    }

    private fun cancelNotification(
        date: String,
        time: String,
        type: EventType,
        name: String,
        reminderMinutes: Int
    ) {
        val pendingIntent = getPendingIntent(date, time, type, name, reminderMinutes) ?: return

        val notification = alarmManager.cancel(pendingIntent.first)
        Timber.d("Notification cancelled: $type, $name, $notification")
    }

    private fun parseDateTime(dateString: String, timeString: String): Calendar {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val date = dateFormat.parse("$dateString $timeString")!!

        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date

        return calendar
    }

    private fun scheduleAlarm(triggerAtMillis: Long, pendingIntent: PendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Timber.d("Missing permissions, disabling notifications...")

                scope.launch {
                    appPrefs.saveNotificationPreference(false)
                }

                return
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
        Timber.d("Alarm for $pendingIntent is scheduled")
    }

    private fun getPendingIntent(
        date: String,
        time: String,
        type: EventType,
        name: String,
        reminderMinutes: Int
    ): Pair<PendingIntent, Calendar>? {
        val calendar = parseDateTime(date, time)
        calendar.add(Calendar.MINUTE, -reminderMinutes)

        if (calendar.timeInMillis <= System.currentTimeMillis()) return null

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("TYPE", type)
            putExtra("NAME", name)
            putExtra("TIME", reminderMinutes.toString())
        }
        val reqCode = (date + time + type + name + reminderMinutes).hashCode()
        val pendingIntentAndTime =
            PendingIntent.getBroadcast(context, reqCode, intent, PendingIntent.FLAG_IMMUTABLE) to calendar
        return pendingIntentAndTime
    }

    companion object {
        private const val CHANNEL_CLASS_REMINDER = "class_reminder_channel"
        private const val CHANNEL_EXAM_REMINDER = "exam_reminder_channel"
    }
}