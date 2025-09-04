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
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import lnx.jetitable.R
import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.datastore.ScheduleDataStore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Serializable
enum class EventType { CLASS, EXAM }

class NotifManager(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val appPrefs = AppPreferences(context)
    private val scheduleDataStore = ScheduleDataStore(context)
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
            Log.d(MANAGER_NAME, "Permissions are not granted, disabling notifications... $isNotifsEnabled, $exactAlarmPermission, $notifPermission")
            return
        }

        if (isNotifsEnabled) {
            val classPrefs = appPrefs.getClassPreferences().first()
            val classList = scheduleDataStore.getClassList().first()
            val examPrefs = appPrefs.getExamPreferences().first()
            val examList = scheduleDataStore.getExamList().first()

            if (classPrefs.isEnabled) {
                classList.forEach { classData ->
                    scheduleNotification(
                        classData.date,
                        classData.start,
                        EventType.CLASS,
                        classData.name,
                        classPrefs.minutes
                    )
                }
            } else {
                classList.forEach { classData ->
                    cancelNotification(
                        classData.date,
                        classData.start,
                        EventType.CLASS,
                        classData.name,
                        classPrefs.minutes
                    )
                }
            }

            if (examPrefs.isEnabled) {
                examList.forEach { examData ->
                    scheduleNotification(
                        examData.date,
                        examData.time,
                        EventType.EXAM,
                        examData.name,
                        examPrefs.minutes
                    )
                }
            } else {
                examList.forEach { examData ->
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
            val pendingIntent = getPendingIntent(date, time, type, name, reminderMinutes)
            if (pendingIntent == null) return

            scheduleAlarm(pendingIntent.second.timeInMillis, pendingIntent.first)
        } catch (e: Exception) {
            Log.e(MANAGER_NAME, "Error scheduling $type notification", e)
        }
    }

    private fun cancelNotification(
        date: String,
        time: String,
        type: EventType,
        name: String,
        reminderMinutes: Int
    ) {
        val pendingIntent = getPendingIntent(date, time, type, name, reminderMinutes)
        if (pendingIntent == null) return

        val cancelledNotif  = alarmManager.cancel(pendingIntent.first)
        Log.d(MANAGER_NAME, "Notification cancelled: $type, $name, $cancelledNotif")
    }

    private fun parseDateTime(dateString: String, timeString: String): Calendar {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val date = dateFormat.parse("$dateString $timeString")!!

        val calendar = Calendar.getInstance()
        calendar.time = date

        Log.d(MANAGER_NAME, "Date and calendar: $date, $calendar")
        return calendar
    }

    private fun scheduleAlarm(triggerAtMillis: Long, pendingIntent: PendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
                Log.d(MANAGER_NAME, "Alarm for  scheduled")
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
            Log.d(MANAGER_NAME, "Alarm scheduled")
        }
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
        private const val MANAGER_NAME = "ScheduleNotificationManager"
        private const val CHANNEL_CLASS_REMINDER = "class_reminder_channel"
        private const val CHANNEL_EXAM_REMINDER = "exam_reminder_channel"
    }
}