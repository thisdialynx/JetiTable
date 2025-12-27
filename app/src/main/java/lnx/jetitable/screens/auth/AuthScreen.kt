package lnx.jetitable.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import lnx.jetitable.R
import lnx.jetitable.misc.DataState
import lnx.jetitable.viewmodel.AuthViewModel

enum class UrlIconItem(val icon: ImageVector, val description: Int, val shortUri: String) {
    GITHUB(lnx.jetitable.ui.icons.Github, R.string.github, "github.com/thisdialynx/JetiTable"),
    UNIVERSITY(lnx.jetitable.ui.icons.Snu, R.string.university, "snu.edu.ua"),
    TIMETABLE(lnx.jetitable.ui.icons.google.CalendarMonth, R.string.timetable, "timetable.lond.lg.ua"),
}

@Composable
fun AuthScreen(onAuthComplete: () -> Unit) {
    val viewModel = viewModel<AuthViewModel>()
    val connectionState by viewModel.isConnected.collectAsStateWithLifecycle()

    AuthUI(
        onAuthComplete = onAuthComplete,
        onPasswordUpdate = { viewModel.updatePassword(it) },
        onEmailUpdate = { viewModel.updateEmail(it) },
        onCredentialsCheck = { viewModel.checkCredentials() },
        onEmailSend = { viewModel.sendEmail() },
        passwordState = viewModel.password,
        emailState = viewModel.email,
        authState = viewModel.authState,
        emailRequestState = viewModel.emailRequestState
    )
}

@Preview
@Composable
private fun AuthScreenPreview() {
    AuthUI(
        onAuthComplete = {},
        onPasswordUpdate = {},
        onEmailUpdate = {},
        onCredentialsCheck = {},
        onEmailSend = {},
        passwordState = "",
        emailState = "",
        authState = DataState.Empty,
        emailRequestState = DataState.Empty
    )
}