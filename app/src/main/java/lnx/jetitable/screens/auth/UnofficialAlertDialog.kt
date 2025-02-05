package lnx.jetitable.screens.auth

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import lnx.jetitable.R

@Composable
fun UnofficialAlertDialog(isOpen: MutableState<Boolean>) {
    val activityContext = LocalActivity.current

    if (isOpen.value) {
        AlertDialog(
            onDismissRequest = { activityContext?.finishAffinity() },
            confirmButton = {
                Button(
                    onClick = { isOpen.value = false },
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