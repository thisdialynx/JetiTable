package lnx.jetitable.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import kotlin.reflect.KFunction1

@Composable
fun PasswordRecoverDialog(
    isOpen: MutableState<Boolean>,
    sendEmail: () -> Unit,
    updateEmail: KFunction1<String, Unit>,
    email: String,
) {
    if (isOpen.value) {
        AlertDialog(
            onDismissRequest = { isOpen.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        sendEmail()
                        isOpen.value = false
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
                        value = email,
                        onValueChange = updateEmail,
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
                            Icon(imageVector = lnx.jetitable.ui.icons.google.Mail, contentDescription = null)
                        }
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { isOpen.value = false }) { Text(text = stringResource(id = R.string.dismiss)) }
            },
            icon = { Icon(imageVector = lnx.jetitable.ui.icons.google.Password, contentDescription = null) }
        )
    }
}