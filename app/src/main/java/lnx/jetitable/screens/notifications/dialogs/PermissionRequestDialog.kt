package lnx.jetitable.screens.notifications.dialogs

import android.content.Intent
import android.os.Build
import android.provider.Settings
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import lnx.jetitable.R
import lnx.jetitable.ui.icons.google.Warning

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestDialog(
    dialogVisibility: Boolean,
    onDismissRequest: () -> Unit,
    notificationPermissionState: PermissionState,
    alarmPermissionState: Boolean,
    onConfirmButtonPress: () -> Unit,
    onExactAlarmPermissionRequest: () -> Unit
) {
    val context = LocalContext.current

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
                            isPermissionGranted = notificationPermissionState.status.isGranted,
                            permissionResId = R.string.post_notifications_permission_dialog,
                            onClick = {
                                if (!notificationPermissionState.status.shouldShowRationale) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                        }
                                        context.startActivity(intent)
                                    }
                                } else {
                                    notificationPermissionState.launchPermissionRequest()
                                }
                            }
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
                    enabled = alarmPermissionState && notificationPermissionState.status.isGranted
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
                text = if (isPermissionGranted) {
                    stringResource(id = R.string.granted_button_title)
                } else {
                    stringResource(id = R.string.grant_button_title)
                },
                fontWeight = FontWeight.Bold
            )
        }
    }

}