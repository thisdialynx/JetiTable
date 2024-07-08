package lnx.jetitable.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import lnx.jetitable.R
import lnx.jetitable.timetable.api.login.AuthViewModel

@Composable
fun AuthScreen() {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

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
                    onValueChange = { authViewModel.updateLogin(it) },
                    label = { Text(text = stringResource(id = R.string.corporate_email_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    placeholder = { Text(text = "example@snu.edu.ua")}
                )
                Text(
                    text = stringResource(id = R.string.corporate_email_description),
                    fontSize = 14.sp
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = authViewModel.password,
                    onValueChange = { authViewModel.updatePassword(it)},
                    label = { Text(text = stringResource(id = R.string.password_label)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Text(
                    modifier = Modifier.clickable {
                            Toast.makeText(
                                    context,
                                    "This feature is not implemented yet",
                                    Toast.LENGTH_SHORT
                                ).show()
                        },
                    text = stringResource(id = R.string.forgot_password_label),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.padding(12.dp))

            Button(
                onClick = {
                    authViewModel.run {
                        updateLogin(login)
                        updatePassword(password)
                        checkPassword()
                    }
                },
            ) {
                Text(text = stringResource(id = R.string.sign_in))
            }

        }
    }
}

@Preview
@Composable
private fun AuthScreenPreview() {
    AuthScreen()
}

