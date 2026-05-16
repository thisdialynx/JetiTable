package lnx.jetitable.features.settings.domain.repository

import lnx.jetitable.features.settings.domain.model.UpdateResult

interface AppUpdateRepository {
    suspend fun checkForUpdates(currentVersion: String): UpdateResult
}