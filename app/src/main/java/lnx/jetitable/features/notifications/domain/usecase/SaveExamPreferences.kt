package lnx.jetitable.features.notifications.domain.usecase

import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.services.notification.NotifManager
import javax.inject.Inject

class SaveExamPreferences @Inject constructor(
    private val appPrefs: AppPreferences,
    private val notifManager: NotifManager
) {
    suspend operator fun invoke(isEnabled: Boolean? = null, minutes: Int? = null) {
        appPrefs.saveExamPreferences(minutes, isEnabled)
        notifManager.updateNotificationSchedules()
    }
}