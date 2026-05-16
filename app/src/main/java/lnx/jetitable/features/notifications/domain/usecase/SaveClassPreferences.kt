package lnx.jetitable.features.notifications.domain.usecase

import jakarta.inject.Inject
import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.services.notification.NotifManager

class SaveClassPreferences @Inject constructor(
    private val appPrefs: AppPreferences,
    private val notifManager: NotifManager
) {
    suspend operator fun invoke(isEnabled: Boolean? = null, minutes: Int? = null) {
        appPrefs.saveClassPreferences(minutes, isEnabled)
        notifManager.updateNotificationSchedules()
    }
}