package lnx.jetitable.features.notifications.presentation

import lnx.jetitable.datastore.AppPreferences

data class NotificationsScreenState(
    val hasNotificationPermission: Boolean = false,
    val hasExactAlarmPermission: Boolean = false,
    val isAppNotificationEnabled: Boolean = false,
    val isPermissionDialogVisible: Boolean = false,
    val classPrefs: AppPreferences.ReminderPrefs = AppPreferences.ReminderPrefs(15, false),
    val examPrefs: AppPreferences.ReminderPrefs = AppPreferences.ReminderPrefs(15, false)
)
