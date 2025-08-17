package lnx.jetitable.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.services.notification.NotifManager

class NotifViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val appPrefs = AppPreferences(context)
    private val notifManager = NotifManager(context)
    val notificationPreference = appPrefs.getNotificationPreference()

    fun enableNotifications() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appPrefs.saveNotificationPreference(true)
            }
        }
    }

    fun disableNotifications() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appPrefs.saveNotificationPreference(false)
            }
        }
    }
    fun disableClassNotifications() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appPrefs.saveClassPreferences(isEnabled = false)
                notifManager.updateNotificationSchedules()

                Log.d(VIEW_MODEL_NAME, "Class notifications disabled")
            }
        }
    }

    fun disableExamNotifications() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appPrefs.saveExamPreferences(isEnabled = false)
                notifManager.updateNotificationSchedules()

                Log.d(VIEW_MODEL_NAME, "Exam notifications disabled")
            }
        }
    }

    fun enableClassNotifications() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appPrefs.saveClassPreferences(isEnabled = true)
                notifManager.updateNotificationSchedules()

                Log.d(VIEW_MODEL_NAME, "Class notifications enabled")
            }
        }
    }

    fun enableExamNotifications() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appPrefs.saveExamPreferences(isEnabled = true)
                notifManager.updateNotificationSchedules()

                Log.d(VIEW_MODEL_NAME, "Exam notifications enabled")
            }
        }
    }

    companion object {
        private const val VIEW_MODEL_NAME = "NotifViewModel"
    }
}