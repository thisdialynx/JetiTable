package lnx.jetitable.features.notifications.domain.models

import lnx.jetitable.datastore.AppPreferences

data class NotificationPreferences(
    val classPrefs: AppPreferences.ReminderPrefs,
    val examPrefs: AppPreferences.ReminderPrefs,
    val appNotifPref: Boolean
)