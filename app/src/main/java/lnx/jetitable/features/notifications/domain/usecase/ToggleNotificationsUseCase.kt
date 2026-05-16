package lnx.jetitable.features.notifications.domain.usecase

import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.services.notification.NotifManager
import javax.inject.Inject

class ToggleNotificationsUseCase @Inject constructor(
    private val appPrefs: AppPreferences,
    private val notifManager: NotifManager
) {

    suspend operator fun invoke(isEnabled: Boolean) {
        if (!isEnabled) {
            appPrefs.saveExamPreferences(isEnabled = false)
            appPrefs.saveClassPreferences(isEnabled = false)
        }

        appPrefs.saveNotificationPreference(isEnabled)
        notifManager.updateNotificationSchedules()
    }
}