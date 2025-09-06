package lnx.jetitable.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.DataState
import lnx.jetitable.screens.auth.dialogs.PasswordRecoverDialog
import lnx.jetitable.screens.auth.dialogs.UnofficialAlertDialog

@Composable
fun AuthUI(
    onAuthComplete: () -> Unit,
    onErrorMessageClear: () -> Unit,
    onPasswordUpdate: (String) -> Unit,
    onEmailUpdate: (String) -> Unit,
    onCredentialsCheck: () -> Unit,
    onEmailSend: () -> Unit,
    passwordState: String,
    emailState: String,
    authState: DataState<out Boolean>
) {
    val context = LocalContext.current
    val openPasswordRecover = remember { mutableStateOf(false) }
    val openNoticeDialog = remember { mutableStateOf(true) }

    LaunchedEffect(authState) {
        when (val state = authState) {
            is DataState.Success -> {
                Toast.makeText(context, R.string.authorized, Toast.LENGTH_SHORT).show()
                onAuthComplete()
            }
            is DataState.Error -> {
                Toast.makeText(context, state.messageResId, Toast.LENGTH_SHORT).show()
                onErrorMessageClear()
            }
            else -> {}
        }
    }

    Scaffold { innerPadding ->
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
                Button(onClick = onCredentialsCheck) {
                    Text(text = stringResource(id = R.string.sign_in))
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