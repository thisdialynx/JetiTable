package lnx.jetitable.screens.settings.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.DataState
import lnx.jetitable.ui.components.StateStatus
import lnx.jetitable.ui.icons.google.Check
import lnx.jetitable.ui.icons.google.Download
import lnx.jetitable.ui.icons.google.Info
import lnx.jetitable.ui.icons.google.Upgrade
import lnx.jetitable.viewmodel.AppUpdateInfo
import lnx.jetitable.viewmodel.SettingsViewModel
import lnx.jetitable.viewmodel.UpdateState

@Composable
fun UpdateCard(updateState: UpdateState) {
    val isDialogOpen = remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    Card(
        onClick = {
            if (updateState is UpdateState.Available) {
                isDialogOpen.value = true
            }
        },
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        when (updateState) {
            is UpdateState.Failure -> {
                StateStatus(
                    icon = Info,
                    modifier = Modifier.rotate(180f),
                    description = stringResource(updateState.reasonResId),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            is UpdateState.Latest -> {
                StateStatus(
                    icon = Check,
                    description = stringResource(R.string.latest_version)
                )
            }
            is UpdateState.Available -> {
                StateStatus(
                    icon = Upgrade,
                    description = stringResource(R.string.update_available_card, updateState.data.latestVersion)
                )
            }
            is UpdateState.Loading -> {
                StateStatus(
                    description = stringResource(R.string.loading),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    }

    if (isDialogOpen.value) {
        when (updateState) {
            is UpdateState.Available -> {
                UpdateDialog(
                    appUpdateInfo = updateState.data,
                    onDismiss = { isDialogOpen.value = false },
                    onDownload = {
                        uriHandler.openUri(updateState.data.downloadUrl)
                        isDialogOpen.value = false
                    }
                )
            }
            else -> {
                isDialogOpen.value = false
            }
        }
    }
}

@Composable
fun UpdateDialog(
    appUpdateInfo: AppUpdateInfo,
    onDismiss: () -> Unit,
    onDownload: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDownload,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(stringResource(R.string.download_action))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = stringResource(R.string.cancel_action))
            }
        },
        icon = {
            Icon(
                imageVector = Download,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = stringResource(R.string.update_available_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "${stringResource(R.string.current_version)} ${appUpdateInfo.currentVersion}\n" +
                            "${stringResource(R.string.latest_version)} ${appUpdateInfo.latestVersion}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = appUpdateInfo.releaseNotes,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        iconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        textContentColor = MaterialTheme.colorScheme.onTertiaryContainer
    )
}