package lnx.jetitable.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import lnx.jetitable.misc.DataState
import lnx.jetitable.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onAuthComplete: () -> Unit
) {
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