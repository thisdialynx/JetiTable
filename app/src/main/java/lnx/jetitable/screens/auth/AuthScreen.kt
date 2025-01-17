package lnx.jetitable.screens.auth

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import lnx.jetitable.R
import lnx.jetitable.viewmodel.AuthViewModel

@Composable
fun AuthScreen(onAuthComplete: () -> Unit = {}) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val activityContext = context as? Activity
    val openDialog = remember { mutableStateOf(false) }
    val openNoticeDialog = remember { mutableStateOf(true) }

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
            SignInFields(authViewModel = authViewModel)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = authViewModel::checkCredentials) {
                    Text(text = stringResource(id = R.string.sign_in))
                }
                TextButton(onClick = { openDialog.value = true }) {
                    Text(text = stringResource(id = R.string.forgot_password_label))
                }

                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = { openDialog.value = false },
                        confirmButton = {
                            Button(onClick = {
                                openDialog.value = false
                                authViewModel.sendMail()
                            }) { Text(text = stringResource(id = R.string.send)) }
                        },
                        title = { Text(text = stringResource(id = R.string.forgot_password_dialog_title)) },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(text = stringResource(id = R.string.forgot_password_dialog_description))
                                OutlinedTextField(
                                    value = authViewModel.email,
                                    onValueChange = authViewModel::updateEmail,
                                    label = {
                                        Text(
                                            text = stringResource(id = R.string.corporate_email_label),
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Done,
                                    ),
                                    placeholder = { Text(text = "example@snu.edu.ua")},
                                    singleLine = true,
                                    leadingIcon = {
                                        Icon(imageVector = lnx.jetitable.ui.icons.google.Mail, contentDescription = null)
                                    }
                                )
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { openDialog.value = false }) { Text(text = stringResource(id = R.string.dismiss)) }
                        },
                        icon = { Icon(imageVector = lnx.jetitable.ui.icons.google.Password, contentDescription = null) }
                    )
                }
            }
        }

        if (openNoticeDialog.value) {
            AlertDialog(
                onDismissRequest = { activityContext?.finishAffinity() },
                confirmButton = {
                    Button(
                        onClick = { openNoticeDialog.value = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.continue_action),
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { activityContext?.finishAffinity() }) {
                        Text(
                            text = stringResource(id = R.string.quit),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                },
                icon = {
                    Icon(
                        imageVector = lnx.jetitable.ui.icons.google.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                },
                text = {
                    Text(
                        text = stringResource(id = R.string.unofficial_app_description),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                },
                title = {
                    Box(contentAlignment = Alignment.TopCenter) {
                        Text(
                            text = stringResource(id = R.string.unofficial_app),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        }
    }
}

@Composable
fun LogoAppTitle() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = lnx.jetitable.ui.icons.Snu,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Column {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = stringResource(id = R.string.app_name_description),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun SignInFields(authViewModel: AuthViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = authViewModel.email,
            onValueChange = authViewModel::updateEmail,
            label = {
                Text(
                    text = stringResource(id = R.string.corporate_email_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            placeholder = { Text(text = "example@snu.edu.ua") },
            singleLine = true,
            leadingIcon = { Icon(imageVector = lnx.jetitable.ui.icons.google.Mail, contentDescription = "Mail icon") }
        )
        Text(
            text = stringResource(id = R.string.corporate_email_description),
            style = MaterialTheme.typography.labelMedium
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = authViewModel.password,
            onValueChange = authViewModel::updatePassword,
            label = {
                Text(
                    text = stringResource(id = R.string.password_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { authViewModel.checkCredentials() }),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = lnx.jetitable.ui.icons.google.Password,
                    contentDescription = "Key icon"
                )
            }
        )
    }
}
