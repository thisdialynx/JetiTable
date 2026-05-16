@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package lnx.jetitable.features.notifications.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import lnx.jetitable.R
import lnx.jetitable.features.notifications.presentation.cards.EventOptionsCard
import lnx.jetitable.features.notifications.presentation.dialogs.PermissionRequestDialog
import lnx.jetitable.ui.components.ThemedSnackbar
import lnx.jetitable.ui.icons.google.CalendarMonth
import lnx.jetitable.ui.icons.google.ContractEdit

@SuppressLint("InlinedApi")
@Composable
fun NotificationLayout(
    onBack: () -> Unit,
    screenState: NotificationsScreenState,
    onNotificationSwitchChange: (Boolean) -> Unit,
    onClassSwitchToggle: (Boolean) -> Unit,
    onExamSwitchToggle: (Boolean) -> Unit,
    onClassTimeSelected: (Int) -> Unit,
    onExamTimeSelected: (Int) -> Unit,
    onDialogVisibilityToggle: (Boolean) -> Unit,
    onNotificationPermissionRequest: () -> Unit,
    onExactAlarmPermissionRequest: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                ThemedSnackbar(it)
            }
        },
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(R.string.notifications_settings_entry_title)) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.notifications_screen_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        text = stringResource(id = R.string.enable_notifications)
                    )
                    Switch(
                        checked = screenState.isAppNotificationEnabled,
                        onCheckedChange = onNotificationSwitchChange
                    )
                }
            }

            PermissionRequestDialog(
                dialogVisibility = screenState.isPermissionDialogVisible,
                onDismissRequest = { onDialogVisibilityToggle(false) },
                notificationPermissionState = screenState.hasNotificationPermission,
                alarmPermissionState = screenState.hasExactAlarmPermission,
                onConfirmButtonPress = {
                    onDialogVisibilityToggle(false)
                    onNotificationSwitchChange(true)
                },
                onExactAlarmPermissionRequest = onExactAlarmPermissionRequest,
                onNotificationPermissionRequest = onNotificationPermissionRequest
            )
            EventOptionsCard(
                icon = CalendarMonth,
                title = stringResource(R.string.activity_type_classes),
                enabled = screenState.isAppNotificationEnabled,
                checked = screenState.classPrefs.isEnabled,
                selectedMinutes = screenState.classPrefs.minutes,
                onCheckedChange = onClassSwitchToggle,
                onTimeSelected = onClassTimeSelected
            )
            EventOptionsCard(
                icon = ContractEdit,
                title = stringResource(R.string.activity_type_exams),
                enabled = screenState.isAppNotificationEnabled,
                checked = screenState.examPrefs.isEnabled,
                selectedMinutes = screenState.examPrefs.minutes,
                onCheckedChange = onExamSwitchToggle,
                onTimeSelected = onExamTimeSelected
            )
        }
    }
}