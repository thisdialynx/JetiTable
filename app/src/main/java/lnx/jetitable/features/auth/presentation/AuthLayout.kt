package lnx.jetitable.features.auth.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.features.auth.presentation.dialogs.PasswordRecoverDialog
import lnx.jetitable.features.auth.presentation.dialogs.WarningDialog
import lnx.jetitable.ui.components.ThemedSnackbar

@Composable
fun AuthLayout(
    password: String,
    login: String,
    showPasswordRecoverDialog: Boolean,
    showWarningDialog: Boolean,
    snackbarHostState: SnackbarHostState,
    authState: AuthStatus,
    onPasswordChange: (String) -> Unit,
    onLoginChange: (String) -> Unit,
    updatePasswordRecoverDialogState: (Boolean) -> Unit,
    updateWarningDialogState: (Boolean) -> Unit,
    onCredentialsCheck: () -> Unit,
    onEmailSend: () -> Unit
) {

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { ThemedSnackbar(it) }
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            LogoAppTitle()

            Spacer(modifier = Modifier.height(12.dp))

            SignInFields(
                onEmailUpdate = onLoginChange,
                onPasswordUpdate = onPasswordChange,
                checkCredentials = onCredentialsCheck,
                email = login,
                password = password
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onCredentialsCheck,
                    enabled = authState != AuthStatus.Loading
                ) {
                    AnimatedContent(
                        targetState = authState
                    ) {
                        when (it) {
                            AuthStatus.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(ButtonDefaults.IconSize),
                                    strokeCap = StrokeCap.Round,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 3.dp
                                )
                            }

                            AuthStatus.Success -> {
                                Icon(
                                    imageVector = Icons.Rounded.Done,
                                    contentDescription = null,
                                    modifier = Modifier.size(ButtonDefaults.IconSize),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            else -> {
                                Text(text = stringResource(id = R.string.sign_in))
                            }
                        }
                    }
                }
                TextButton(onClick = { updatePasswordRecoverDialogState(true) }) {
                    Text(text = stringResource(id = R.string.forgot_password_label))
                }
            }
        }

        PasswordRecoverDialog(
            login = login,
            isOpen = showPasswordRecoverDialog,
            onVisibilityChange = updatePasswordRecoverDialogState,
            onEmailSend = onEmailSend,
            onLoginChange = onLoginChange
        )

        WarningDialog(showWarningDialog, updateWarningDialogState)
    }
}