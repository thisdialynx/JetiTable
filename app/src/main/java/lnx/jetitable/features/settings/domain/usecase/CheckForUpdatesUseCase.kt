package lnx.jetitable.features.settings.domain.usecase

import lnx.jetitable.features.settings.domain.model.UpdateResult
import lnx.jetitable.features.settings.domain.repository.AppUpdateRepository
import javax.inject.Inject

class CheckForUpdatesUseCase @Inject constructor(
    private val appUpdateRepository: AppUpdateRepository
) {
    suspend operator fun invoke(currentVersion: String): UpdateResult {
        return appUpdateRepository.checkForUpdates(currentVersion)
    }
}