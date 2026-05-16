package lnx.jetitable.features.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lnx.jetitable.api.timetable.domain.models.EducationForm
import lnx.jetitable.api.timetable.domain.models.SemesterType
import lnx.jetitable.features.settings.domain.model.AppUpdateState
import lnx.jetitable.features.settings.domain.model.SettingsEvent
import lnx.jetitable.features.settings.domain.model.UpdateResult
import lnx.jetitable.features.settings.domain.model.UserInfoData
import lnx.jetitable.features.settings.domain.model.UserProfileState

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onDestinationNavigate: (String) -> Unit,
    onNavigateToAuth: () -> Unit
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.logOutEvent.collect {
            when (it) {
                is SettingsEvent.NavigateToAuth -> onNavigateToAuth()
            }
        }
    }

    SettingsLayout(
        screenState = screenState,
        onBack = onBack,
        onDestinationNavigate = onDestinationNavigate,
        onLogOut = viewModel::onLogOutClick
    )
}


@Preview
@Composable
private fun AboutScreenPreview() {
    SettingsLayout(
        screenState = SettingsScreenState(
            UserProfileState.Success(
                UserInfoData(
                    fullName = "Chyrynkov Mykyta Hlibovych",
                    group = "ZHO-94",
                    academicYears = "2067/2069",
                    status = "Student",
                    educationForm = EducationForm.PART_TIME,
                    semesterType = SemesterType.SPRING
                )
            ),
            UpdateResult.Available(
                AppUpdateState(
                    currentVersion = "1.3.7",
                    latestVersion = "2.2.8",
                    updateAvailable = true,
                    downloadUrl = "",
                    releaseNotes = "Say wallahi bro, say wallahi! (no brainrot anymore I swear)"
                )
            )
        ),
        onBack = {},
        onDestinationNavigate = {},
        onLogOut = {}
    )
}