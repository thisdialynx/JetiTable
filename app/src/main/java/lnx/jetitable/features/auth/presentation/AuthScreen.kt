package lnx.jetitable.features.auth.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lnx.jetitable.R
import lnx.jetitable.features.auth.domain.model.AuthError
import lnx.jetitable.features.auth.domain.model.RecoveryError

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onAuthComplete: () -> Unit
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val emailRequestState by viewModel.emailRequestState.collectAsStateWithLifecycle()
    val login by viewModel.login.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val showWarningDialog by viewModel.showWarningDialog.collectAsStateWithLifecycle()
    val showPasswordRecoverDialog by viewModel.showPasswordRecoverDialog.collectAsStateWithLifecycle()

    val resources = LocalResources.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = authState, key2 = emailRequestState) {
        when (authState) {
            is AuthStatus.Error -> {
                val message = when ((authState as AuthStatus.Error).error) {
                    AuthError.INVALID_CREDENTIALS -> resources.getString(R.string.wrong_credentials)
                    AuthError.NETWORK_ERROR -> resources.getString(R.string.network_error)
                    AuthError.UNKNOWN_ERROR -> resources.getString(R.string.unknown_error)
                    AuthError.EMPTY_RESPONSE -> resources.getString(R.string.network_error)
                    AuthError.NOT_CORPORATE_EMAIL -> resources.getString(R.string.corporate_email_error)
                }
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }

            AuthStatus.Success -> onAuthComplete()
            else -> {}
        }
    }

    LaunchedEffect(key1 = emailRequestState) {
        when (emailRequestState) {
            is EmailRequestStatus.Error -> {
                val message = when ((emailRequestState as EmailRequestStatus.Error).error) {
                    RecoveryError.INVALID_CREDENTIALS -> resources.getString(R.string.wrong_credentials)
                    RecoveryError.NOT_CORPORATE_EMAIL -> resources.getString(R.string.corporate_email_error)
                    RecoveryError.NETWORK_ERROR -> resources.getString(R.string.network_error)
                    RecoveryError.UNKNOWN_ERROR -> resources.getString(R.string.unknown_error)
                }
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }

            EmailRequestStatus.Success -> TODO()
            else -> {}
        }
    }

    AuthLayout(
        password = password,
        login = login,
        showPasswordRecoverDialog = showPasswordRecoverDialog,
        showWarningDialog = showWarningDialog,
        authState = authState,
        snackbarHostState = snackbarHostState,
        onPasswordChange = viewModel::updatePassword,
        onLoginChange = viewModel::updateLogin,
        updatePasswordRecoverDialogState = viewModel::updatePasswordRecoverDialogState,
        updateWarningDialogState = viewModel::updateWarningDialogState,
        onCredentialsCheck = viewModel::onLoginClick,
        onEmailSend = viewModel::onEmailSend,
    )
}

@Preview
@Composable
private fun AuthScreenPreview() {
    AuthLayout(
        password = "Siemiechki",
        login = "k0cheryzhk4@snu.edu.ua",
        showPasswordRecoverDialog = false,
        showWarningDialog = false,
        snackbarHostState = SnackbarHostState(),
        authState = AuthStatus.Idle,
        onPasswordChange = { },
        onLoginChange = { },
        updatePasswordRecoverDialogState = { },
        updateWarningDialogState = { },
        onCredentialsCheck = { },
        onEmailSend = { }
    )
}