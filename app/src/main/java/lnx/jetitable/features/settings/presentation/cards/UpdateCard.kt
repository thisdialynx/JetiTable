package lnx.jetitable.features.settings.presentation.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.features.settings.domain.model.AppUpdateState
import lnx.jetitable.features.settings.domain.model.UpdateCheckError
import lnx.jetitable.features.settings.domain.model.UpdateResult
import lnx.jetitable.ui.components.StateStatus
import lnx.jetitable.ui.icons.google.Check
import lnx.jetitable.ui.icons.google.Download
import lnx.jetitable.ui.icons.google.Error
import lnx.jetitable.ui.icons.google.Upgrade

@Composable
fun UpdateCard(updateResult: UpdateResult) {
    val isDialogOpen = remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val onTertiaryContainerColor = MaterialTheme.colorScheme.onTertiaryContainer

    Card(
        onClick = {
            if (updateResult is UpdateResult.Available) {
                isDialogOpen.value = true
            }
        },
        colors = CardDefaults.cardColors(
            contentColor = onTertiaryContainerColor,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        when (updateResult) {
            is UpdateResult.Failure -> {
                val descResId = when (updateResult.error) {
                    UpdateCheckError.NETWORK_ERROR -> R.string.network_error
                    UpdateCheckError.UNKNOWN_ERROR -> R.string.unknown_error
                }

                StateStatus(
                    icon = Error,
                    description = stringResource(descResId),
                    color = onTertiaryContainerColor
                )
            }

            is UpdateResult.Latest -> {
                StateStatus(
                    icon = Check,
                    description = stringResource(R.string.no_updates),
                    color = onTertiaryContainerColor
                )
            }

            is UpdateResult.Available -> {

                StateStatus(
                    icon = Upgrade,
                    description = stringResource(
                        R.string.update_available_card,
                        updateResult.data.latestVersion
                    ),
                    color = onTertiaryContainerColor
                )
            }

            is UpdateResult.Loading -> {
                StateStatus(
                    description = stringResource(R.string.loading),
                    color = onTertiaryContainerColor
                )
            }
        }
    }

    if (isDialogOpen.value) {
        when (updateResult) {
            is UpdateResult.Available -> {
                UpdateDialog(
                    appUpdateState = updateResult.data,
                    onDismiss = { isDialogOpen.value = false },
                    onDownload = {
                        uriHandler.openUri(updateResult.data.downloadUrl)
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
    appUpdateState: AppUpdateState,
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
                    text = "${stringResource(R.string.current_version)} ${appUpdateState.currentVersion}\n" +
                            "${stringResource(R.string.latest_version)} ${appUpdateState.latestVersion}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = appUpdateState.releaseNotes,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        iconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        textContentColor = MaterialTheme.colorScheme.onTertiaryContainer
    )
}