package lnx.jetitable.features.settings.domain.usecase

import lnx.jetitable.datastore.AppPreferences
import lnx.jetitable.datastore.CookieDataStore
import lnx.jetitable.datastore.ScheduleDataStore
import lnx.jetitable.datastore.UserInfoStore
import lnx.jetitable.misc.AndroidSyncManager
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val userInfoStore: UserInfoStore,
    private val cookieDataStore: CookieDataStore,
    private val scheduleDataStore: ScheduleDataStore,
    private val appPrefs: AppPreferences,
    private val androidSyncManager: AndroidSyncManager
) {

    suspend operator fun invoke() {
        userInfoStore.clearAll()
        cookieDataStore.clearCookies()
        scheduleDataStore.clearAll()
        appPrefs.clearAll()
        androidSyncManager.stopSync()
    }
}