package lnx.jetitable.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import lnx.jetitable.R
import lnx.jetitable.misc.DataState
import lnx.jetitable.viewmodel.AppUpdateInfo
import lnx.jetitable.viewmodel.SettingsViewModel
import lnx.jetitable.viewmodel.UpdateState
import lnx.jetitable.viewmodel.UserInfoData
import lnx.jetitable.viewmodel.UserInfoState

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit, onDestinationNavigate: (String) -> Unit
) {
    val userInfoState by viewModel.userInfoState.collectAsStateWithLifecycle()
    val updateInfo by viewModel.updateInfo.collectAsStateWithLifecycle()

    SettingsUI(
        onBack = onBack,
        userInfoState = userInfoState,
        onDestinationNavigate = onDestinationNavigate,
        updateState = updateInfo,
        onSignOut = { viewModel.signOut() }
    )
}

@Preview
@Composable
private fun NoAccountPreview() {
    SettingsUI(
        onBack = {},
        userInfoState = UserInfoState.Loading,
        onDestinationNavigate = {},
        updateState = UpdateState.Latest,
        onSignOut = {}
    )
}

@Preview
@Composable
private fun AboutScreenPreview() {
    SettingsUI(
        onBack = {},
        userInfoState = UserInfoState.Success(
            UserInfoData(
                fullName = "Jane Doe" to 0,
                group = "IPZ-23d" to "1122",
                formOfEducationResId = R.string.full_time,
                academicYears = "2001/2002",
                semesterResId = R.string.spring_semester,
                status = "student"
            )
        ),
        onDestinationNavigate = {},
        updateState = UpdateState.Latest,
        onSignOut = {}
    )
}