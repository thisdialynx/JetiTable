package lnx.jetitable.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import lnx.jetitable.R

@Composable
fun AuthScreen(
    onAuthComplete: () -> Unit = {},
) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }

    LaunchedEffect(authViewModel.isAuthorized) {
        if (authViewModel.isAuthorized) {
            Toast.makeText(context, R.string.authorized, Toast.LENGTH_SHORT).show()
            onAuthComplete()
        }
    }
    LaunchedEffect(authViewModel.errorMessage) {
        if (authViewModel.errorMessage != 0) {
            Toast.makeText(context, authViewModel.errorMessage, Toast.LENGTH_SHORT).show()
            authViewModel.clearErrorMessage()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                stringResource(id = R.string.app_name),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
            )
            Text(
                stringResource(id = R.string.app_name_description),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.padding(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = authViewModel.login,
                    onValueChange = authViewModel::updateLogin,
                    label = { Text(text = stringResource(id = R.string.corporate_email_label)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    placeholder = { Text(text = "example@snu.edu.ua")},
                    singleLine = true
                )
                Text(
                    text = stringResource(id = R.string.corporate_email_description),
                    fontSize = 14.sp
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = authViewModel.password,
                    onValueChange = authViewModel::updatePassword,
                    label = { Text(text = stringResource(id = R.string.password_label)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { authViewModel.checkCredentials() }
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.padding(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = authViewModel::checkCredentials
                ) {
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
                                    value = authViewModel.login,
                                    onValueChange = authViewModel::updateLogin,
                                    label = { Text(text = stringResource(id = R.string.corporate_email_label)) },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Done
                                    ),
                                    placeholder = { Text(text = "example@snu.edu.ua")},
                                    singleLine = true
                                )
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { openDialog.value = false }) {
                                Text(text = stringResource(id = R.string.dismiss))
                            }
                        }
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun AuthScreenPreview() {
    AuthScreen()
}

