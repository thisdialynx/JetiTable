package lnx.jetitable.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import lnx.jetitable.R

@Composable
fun SignInFields(
    onEmailUpdate: (String) -> Unit,
    onPasswordUpdate: (String) -> Unit,
    checkCredentials: () -> Unit,
    password: String,
    email: String
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailUpdate,
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
            leadingIcon = { Icon(imageVector = lnx.jetitable.ui.icons.google.Mail, contentDescription = "Mail icon") },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentType = ContentType.Username }
        )
        Text(
            text = stringResource(id = R.string.corporate_email_description),
            style = MaterialTheme.typography.labelMedium
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordUpdate,
            label = {
                Text(
                    text = stringResource(id = R.string.password_label),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { checkCredentials() }),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = lnx.jetitable.ui.icons.google.Password,
                    contentDescription = "Key icon"
                )
            },
            trailingIcon = {
                val visibilityIcon: Pair<ImageVector, Int> = if (passwordVisible) lnx.jetitable.ui.icons.google.Visiblity to R.string.show_password
                else lnx.jetitable.ui.icons.google.VisibilityOff to R.string.hide_password
                IconButton(onClick = {passwordVisible = !passwordVisible}) {
                    Icon(
                        imageVector = visibilityIcon.first,
                        contentDescription = stringResource(id = visibilityIcon.second)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentType = ContentType.Password }
        )
    }
}