package lnx.jetitable.screens.settings

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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.DataState
import lnx.jetitable.viewmodel.SettingsViewModel

@Composable
fun UpdateCard(updateInfo: DataState<out SettingsViewModel.AppUpdateInfo>) {
    val isDialogOpen = remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    Card(
        onClick = {
            if (updateInfo is DataState.Success || updateInfo is DataState.Empty) {
                isDialogOpen.value = true
            }
        },
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (updateInfo) {
                is DataState.Error -> {
                    Icon(
                        imageVector = lnx.jetitable.ui.icons.google.Warning,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.could_not_check_for_updates),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                is DataState.Empty -> {
                    Icon(
                        imageVector = lnx.jetitable.ui.icons.google.Check,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.no_updates),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                is DataState.Success -> {
                    Icon(
                        imageVector = lnx.jetitable.ui.icons.google.Upgrade,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.update_available_card),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                is DataState.Loading -> {
                    CircularProgressIndicator(
                        strokeCap = StrokeCap.Round,
                        modifier = Modifier.then(Modifier.size(24.dp)),
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = stringResource(R.string.fetching_updates),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    if (isDialogOpen.value) {
        when (updateInfo) {
            is DataState.Success -> {
                UpdateDialog(
                    appUpdateInfo = updateInfo.data,
                    onDismiss = { isDialogOpen.value = false },
                    onDownload = {
                        uriHandler.openUri(updateInfo.data.downloadUrl)
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
    appUpdateInfo: SettingsViewModel.AppUpdateInfo,
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
                imageVector = lnx.jetitable.ui.icons.google.Download,
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