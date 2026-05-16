package lnx.jetitable.features.settings.presentation

import lnx.jetitable.features.settings.domain.model.SettingsEntries
import lnx.jetitable.features.settings.domain.model.UpdateResult
import lnx.jetitable.features.settings.domain.model.UserProfileState

data class SettingsScreenState(
    val userInfo: UserProfileState = UserProfileState.Loading,
    val updateInfo: UpdateResult = UpdateResult.Loading,
    val settings: List<SettingsEntries> = emptyList()
)