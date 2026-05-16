package lnx.jetitable.features.settings.domain.usecase

import lnx.jetitable.features.settings.domain.model.SettingsEntries
import lnx.jetitable.features.settings.repository.SettingsRepositoryImpl
import javax.inject.Inject

class GetSettingsListUseCase @Inject constructor(
    private val settingsRepositoryImpl: SettingsRepositoryImpl
) {

    operator fun invoke(): List<SettingsEntries> {
        return settingsRepositoryImpl.getSettingsList()
    }
}