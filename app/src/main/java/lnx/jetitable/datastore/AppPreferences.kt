package lnx.jetitable.datastore

import android.app.NotificationManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("application_preferences")

class AppPreferences(private val context: Context) {

    suspend fun saveNotificationPreference(enable: Boolean) {
        context.dataStore.edit {
            it[IS_NOTIFICATIONS_ENABLED] = enable
        }
    }

    fun getNotificationPreference(): Flow<Boolean> = context.dataStore.data
        .map { it[IS_NOTIFICATIONS_ENABLED] == true }

    suspend fun saveClassPreferences(
        minutes: Int? = null,
        priority: Int? = null,
        isEnabled: Boolean? = null
    ) {
        context.dataStore.edit { dataStore ->
            minutes?.let {
                dataStore[CLASS_REMINDER_MINUTES] = it
            }
            priority?.let {
                dataStore[CLASS_NOTIFICATION_IMPORTANCE] = it
            }
            isEnabled?.let {
                dataStore[IS_CLASS_REMINDER_ENABLED] = it
            }
        }
    }

    fun getClassPreferences(): Flow<ReminderPrefs> = context.dataStore.data
        .map { data ->
        val minutes = data[CLASS_REMINDER_MINUTES] ?: 15
        val importance = data[CLASS_NOTIFICATION_IMPORTANCE] ?: NotificationManager.IMPORTANCE_DEFAULT
        val isEnabled = data[IS_CLASS_REMINDER_ENABLED] == true
        ReminderPrefs(minutes, importance, isEnabled)
    }

    suspend fun saveExamPreferences(
        minutes: Int? = null,
        priority: Int? = null,
        isEnabled: Boolean? = null
    ) {
        context.dataStore.edit { dataStore ->
            minutes?.let {
                dataStore[EXAM_REMINDER_MINUTES] = it
            }
            priority?.let {
                dataStore[EXAM_NOTIFICATION_IMPORTANCE] = it
            }
            isEnabled?.let {
                dataStore[IS_EXAM_REMINDER_ENABLED] = it
            }
        }
    }

    fun getExamPreferences(): Flow<ReminderPrefs> = context.dataStore.data
        .map { data ->
        val minutes = data[EXAM_REMINDER_MINUTES] ?: 15
        val importance = data[EXAM_NOTIFICATION_IMPORTANCE] ?: NotificationManager.IMPORTANCE_HIGH
        val isEnabled = data[IS_EXAM_REMINDER_ENABLED] == true
        ReminderPrefs(minutes, importance, isEnabled)
    }

    suspend fun saveNotificationTipPreference(value: Boolean) = context.dataStore.edit {
        it[SHOW_NOTIFICATION_TIP] = value
    }

    fun getNotificationTipPreference(): Flow<Boolean> = context.dataStore.data
        .map { it[SHOW_NOTIFICATION_TIP] ?: true}

    suspend fun clearAll() {
        context.dataStore.edit {
            it.clear()
        }
    }

    data class ReminderPrefs(
        val minutes: Int = 15,
        val priority: Int = NotificationManager.IMPORTANCE_DEFAULT,
        val isEnabled: Boolean = false
    )

    companion object {
        val CLASS_REMINDER_MINUTES = intPreferencesKey("CLASS_REMINDER_MINUTES")
        val CLASS_NOTIFICATION_IMPORTANCE = intPreferencesKey("CLASS_NOTIFICATION_IMPORTANCE")
        val EXAM_REMINDER_MINUTES = intPreferencesKey("EXAM_REMINDER_MINUTES")
        val EXAM_NOTIFICATION_IMPORTANCE = intPreferencesKey("EXAM_NOTIFICATION_IMPORTANCE")
        val IS_CLASS_REMINDER_ENABLED = booleanPreferencesKey("IS_CLASS_REMINDER_ENABLED")
        val IS_EXAM_REMINDER_ENABLED = booleanPreferencesKey("IS_EXAM_REMINDER_ENABLED")
        val IS_NOTIFICATIONS_ENABLED = booleanPreferencesKey("IS_NOTIFICATIONS_ENABLED")
        val SHOW_NOTIFICATION_TIP = booleanPreferencesKey("SHOW_NOTIFICATION_TIP")
    }
}