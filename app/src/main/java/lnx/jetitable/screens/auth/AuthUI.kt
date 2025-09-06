package lnx.jetitable.screens.auth

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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import lnx.jetitable.R
import lnx.jetitable.misc.DataState
import lnx.jetitable.screens.auth.dialogs.PasswordRecoverDialog
import lnx.jetitable.screens.auth.dialogs.UnofficialAlertDialog
import lnx.jetitable.ui.components.AppSnackbar

@Composable
fun AuthUI(
    onAuthComplete: () -> Unit,
    onPasswordUpdate: (String) -> Unit,
    onEmailUpdate: (String) -> Unit,
    onCredentialsCheck: () -> Unit,
    onEmailSend: () -> Unit,
    passwordState: String,
    emailState: String,
    authState: DataState<out Boolean>,
    emailRequestState: DataState<out Int>
) {
    val context = LocalContext.current
    val openPasswordRecover = remember { mutableStateOf(false) }
    val openNoticeDialog = remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState) {
        when (val state = authState) {
            is DataState.Success -> {
                delay(500)
                onAuthComplete()
            }
            is DataState.Error -> {
                showSnackbar( context.getString(state.messageResId), snackbarHostState)
            }
            else -> {}
        }
    }

    LaunchedEffect(emailRequestState) {
        when (val state = emailRequestState) {
            is DataState.Success -> {
                showSnackbar( context.getString(state.data), snackbarHostState)
            }
            is DataState.Error -> {
                showSnackbar( context.getString(state.messageResId), snackbarHostState)
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                AppSnackbar(
                    snackbarData = it
                )
            }
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
                onEmailUpdate = onEmailUpdate,
                onPasswordUpdate = onPasswordUpdate,
                checkCredentials = onCredentialsCheck,
                email = emailState,
                password = passwordState
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onCredentialsCheck
                ) {
                    AnimatedContent(
                        targetState = authState
                    ) {
                        when (it) {
                            DataState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(ButtonDefaults.IconSize),
                                    strokeCap = StrokeCap.Round,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            DataState.Success(true) -> {
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
                TextButton(onClick = { openPasswordRecover.value = true }) {
                    Text(text = stringResource(id = R.string.forgot_password_label))
                }
            }
        }

        PasswordRecoverDialog(
            isOpen = openPasswordRecover,
            onEmailSend = onEmailSend,
            onEmailUpdate = onEmailUpdate,
            email = emailState
        )
        UnofficialAlertDialog(isOpen = openNoticeDialog)
    }
}

suspend fun showSnackbar(
    message: String,
    snackbarHostState: SnackbarHostState
) {
    snackbarHostState.showSnackbar(
        message = message,
        duration = SnackbarDuration.Short
    )
}