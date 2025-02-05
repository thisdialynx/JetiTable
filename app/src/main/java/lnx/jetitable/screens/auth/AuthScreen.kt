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
import androidx.lifecycle.viewmodel.compose.viewModel
import lnx.jetitable.R
import lnx.jetitable.viewmodel.AuthViewModel

@Composable
fun AuthScreen(onAuthComplete: () -> Unit = {}) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val openPasswordRecover = remember { mutableStateOf(false) }
    val openNoticeDialog = remember { mutableStateOf(true) }
    val password = authViewModel.password
    val email = authViewModel.email

    LaunchedEffect(authViewModel.isAuthorized, authViewModel.errorMessage) {
        if (authViewModel.isAuthorized) {
            Toast.makeText(context, R.string.authorized, Toast.LENGTH_SHORT).show()
            onAuthComplete()
        }
        if (authViewModel.errorMessage != 0) {
            Toast.makeText(context, authViewModel.errorMessage, Toast.LENGTH_SHORT).show()
            authViewModel.clearErrorMessage()
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
                updateEmail = authViewModel::updateEmail,
                updatePassword = authViewModel::updatePassword,
                checkCredentials = authViewModel::checkCredentials,
                email = email,
                password = password
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = authViewModel::checkCredentials) {
                    Text(text = stringResource(id = R.string.sign_in))
                }
                TextButton(onClick = { openPasswordRecover.value = true }) {
                    Text(text = stringResource(id = R.string.forgot_password_label))
                }
            }
        }

        PasswordRecoverDialog(
            isOpen = openPasswordRecover,
            sendEmail = authViewModel::sendMail,
            updateEmail = authViewModel::updateEmail,
            email = email
        )
        UnofficialAlertDialog(isOpen = openNoticeDialog)
    }
}
