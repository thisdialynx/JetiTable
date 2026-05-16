package lnx.jetitable.features.settings.repository

import lnx.jetitable.features.settings.domain.model.SettingsEntries
import lnx.jetitable.features.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {
    override fun getSettingsList(): List<SettingsEntries> {
        return SettingsEntries.entries.toList()
    }
}