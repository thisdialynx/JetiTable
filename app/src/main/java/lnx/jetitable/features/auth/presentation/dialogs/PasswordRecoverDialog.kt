package lnx.jetitable.features.auth.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.ui.icons.google.Mail
import lnx.jetitable.ui.icons.google.Password

@Composable
fun PasswordRecoverDialog(
    login: String,
    isOpen: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    onEmailSend: () -> Unit,
    onLoginChange: (String) -> Unit,
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = { onVisibilityChange(false) },
            confirmButton = {
                Button(
                    onClick = {
                        onEmailSend()
                        onVisibilityChange(false)
                    }
                ) {
                    Text(text = stringResource(id = R.string.send))
                }
            },
            title = { Text(text = stringResource(id = R.string.forgot_password_dialog_title)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = stringResource(id = R.string.forgot_password_dialog_description))
                    OutlinedTextField(
                        value = login,
                        onValueChange = onLoginChange,
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
                        placeholder = { Text(text = "example@snu.edu.ua") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Mail, contentDescription = null)
                        }
                    )
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { onVisibilityChange(false) }) {
                    Text(
                        text = stringResource(
                            id = R.string.dismiss
                        )
                    )
                }
            },
            icon = { Icon(imageVector = Password, contentDescription = null) }
        )
    }
}