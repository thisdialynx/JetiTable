package lnx.jetitable.features.notifications.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import lnx.jetitable.R
import lnx.jetitable.ui.icons.google.Warning

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestDialog(
    dialogVisibility: Boolean,
    notificationPermissionState: Boolean,
    alarmPermissionState: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmButtonPress: () -> Unit,
    onExactAlarmPermissionRequest: () -> Unit,
    onNotificationPermissionRequest: () -> Unit
) {
    if (dialogVisibility) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            icon = {
                Icon(
                    imageVector = Warning,
                    contentDescription = null
                )
            },
            title = {
                Text(
                    text = stringResource(id = R.string.permissions_required_title_dialog)
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.permissions_required_text_dialog)
                    )
                    Column(modifier = Modifier.fillMaxWidth()) {
                        DialogPermission(
                            isPermissionGranted = notificationPermissionState,
                            permissionResId = R.string.post_notifications_permission_dialog,
                            onClick = onNotificationPermissionRequest
                        )

                        DialogPermission(
                            isPermissionGranted = alarmPermissionState,
                            permissionResId = R.string.alarms_and_reminders_permission_dialog,
                            onClick = onExactAlarmPermissionRequest
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirmButtonPress,
                    enabled = alarmPermissionState && notificationPermissionState
                ) {
                    Text(
                        text = "OK"
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismissRequest,
                ) {
                    Text(
                        text = stringResource(id = R.string.dismiss)
                    )
                }
            }
        )
    }
}

@Composable
private fun DialogPermission(
    isPermissionGranted: Boolean,
    permissionResId: Int,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val buttonText = if (isPermissionGranted) {
            stringResource(id = R.string.granted_button_title)
        } else {
            stringResource(id = R.string.grant_button_title)
        }

        Text(
            text = stringResource(id = permissionResId),
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
        )
        TextButton(
            onClick = onClick,
            enabled = !isPermissionGranted
        ) {
            Text(
                text = buttonText,
                fontWeight = FontWeight.Bold
            )
        }
    }

}