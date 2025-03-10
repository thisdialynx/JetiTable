package lnx.jetitable.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import lnx.jetitable.R
import lnx.jetitable.viewmodel.AuthState
import lnx.jetitable.viewmodel.AuthViewModel

enum class UrlIconItem(val icon: ImageVector, val description: Int, val shortUri: String) {
    GITHUB(lnx.jetitable.ui.icons.Github, R.string.github, "github.com/thisdialynx/JetiTable"),
    UNIVERSITY(lnx.jetitable.ui.icons.Snu, R.string.university, "snu.edu.ua"),
    TIMETABLE(lnx.jetitable.ui.icons.google.CalendarMonth, R.string.timetable, "timetable.lond.lg.ua"),
}

enum class ContributorItem(
    val profilePictureUrl: String? = null,
    val title: String,
    val description: Int,
    val icon: ImageVector,
    val iconDescription: String,
    val shortUrl: String
) {
    DIALYNX(
        profilePictureUrl = "https://github.com/thisdialynx.png",
        title = "Dialynx",
        description = R.string.developer,
        icon = lnx.jetitable.ui.icons.Telegram,
        iconDescription = "Telegram",
        shortUrl = "t.me/placeholder"
    ),
    DENYSRATOV(
        title = "Denys Ratov",
        description = R.string.timetable_developer,
        icon = lnx.jetitable.ui.icons.Telegram,
        iconDescription = "Telegram",
        shortUrl = "t.me/placeholder",
    )
}

@Composable
fun AuthScreen(onAuthComplete: () -> Unit) {
    val viewModel = viewModel<AuthViewModel>()
    val connectivityState by viewModel.connectivityState.collectAsStateWithLifecycle()

    AuthUI(
        onAuthComplete = { onAuthComplete() },
        onErrorMessageClear = { viewModel.clearErrorMessage() },
        onPasswordUpdate = { viewModel.updatePassword(it) },
        onEmailUpdate = { viewModel.updateEmail(it) },
        onCredentialsCheck = { viewModel.checkCredentials() },
        onEmailSend = { viewModel.sendEmail() },
        passwordState = viewModel.password,
        emailState = viewModel.email,
        authState = viewModel.authState
    )
}

@Preview
@Composable
private fun AuthScreenPreview() {
    AuthUI(
        onAuthComplete = {},
        onErrorMessageClear = {},
        onPasswordUpdate = {},
        onEmailUpdate = {},
        onCredentialsCheck = {},
        onEmailSend = {},
        passwordState = "",
        emailState = "",
        authState = AuthState.Idle
    )
}