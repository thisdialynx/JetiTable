package lnx.jetitable.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import lnx.jetitable.R
import lnx.jetitable.misc.DataState
import lnx.jetitable.viewmodel.SettingsViewModel
import lnx.jetitable.viewmodel.UserDataUiState

@Composable
fun SettingsScreen(onBack: () -> Unit, onDestinationNavigate: (String) -> Unit) {
    val viewModel = viewModel<SettingsViewModel>()
    val userDataUiState by viewModel.userDataUiState.collectAsStateWithLifecycle()
    val updateInfo by viewModel.updateInfo.collectAsStateWithLifecycle()

    SettingsUI(
        onBack = onBack,
        userDataUiState = userDataUiState,
        onDestinationNavigate = onDestinationNavigate,
        updateInfo = updateInfo,
        onSignOut = { viewModel.signOut() }
    )
}

@Preview
@Composable
private fun NoAccountPreview() {
    SettingsUI(
        onBack = {},
        userDataUiState = DataState.Loading,
        onDestinationNavigate = {},
        updateInfo = DataState.Empty,
        onSignOut = {}
    )
}

@Preview
@Composable
private fun AboutScreenPreview() {
    SettingsUI(
        onBack = {},
        userDataUiState = DataState.Success(
            UserDataUiState(
                fullName = "Jane Doe" to 0,
                group = "IPZ-23d" to "1122",
                formOfEducationResId = R.string.full_time,
                academicYears = "2001/2002",
                semesterResId = R.string.spring_semester,
                status = "student"
            )
        ),
        onDestinationNavigate = {},
        updateInfo = DataState.Success(
            SettingsViewModel.AppUpdateInfo(
                currentVersion = "0.0.0"
            )
        ),
        onSignOut = {}
    )
}