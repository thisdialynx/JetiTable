package lnx.jetitable.features.settings.domain.repository

import lnx.jetitable.features.settings.domain.model.SettingsEntries

interface SettingsRepository {
    fun getSettingsList(): List<SettingsEntries>
}