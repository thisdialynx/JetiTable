package lnx.jetitable.screens.notifications

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import lnx.jetitable.R
import lnx.jetitable.screens.notifications.cards.EventOptionsCard
import lnx.jetitable.screens.notifications.dialogs.PermissionRequestDialog
import lnx.jetitable.screens.notifications.dialogs.PriorityPickerDialog
import lnx.jetitable.ui.components.AppSnackbar
import lnx.jetitable.ui.icons.google.CalendarMonth
import lnx.jetitable.ui.icons.google.ContractEdit

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun NotificationsUI(
    onBack: () -> Unit,
    notificationsEnabled: State<Boolean>,
    onNotificationSwitchChange: (Boolean) -> Unit,
    onClassSwitchChange: (Boolean) -> Unit,
    onExamSwitchChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val isClassPriorityDialogOpened = remember { mutableStateOf(false) }
    var isPermissionRequestDialogOpened by remember { mutableStateOf(false) }
    val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    var alarmPermissionState by remember { mutableStateOf(false) }
    var isClassSettingsEnabled by remember { mutableStateOf(false) }
    var isExamSettingsEnabled by remember { mutableStateOf(false) }
    val alarmPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult())
            { alarmPermissionState = hasExactAlarmPermission(context) }

    LaunchedEffect(Unit) {
        alarmPermissionState = hasExactAlarmPermission(context)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                AppSnackbar(it)
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
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
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
                )
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
                        checked = notificationsEnabled.value && alarmPermissionState && notificationPermissionState.status.isGranted,
                        onCheckedChange = {
                            if (!alarmPermissionState || !notificationPermissionState.status.isGranted) {
                                isPermissionRequestDialogOpened = !isPermissionRequestDialogOpened
                            }
                            onNotificationSwitchChange(it)
                        }
                    )
                }
            }

            PermissionRequestDialog(
                dialogVisibility = isPermissionRequestDialogOpened,
                onDismissRequest = { isPermissionRequestDialogOpened = !isPermissionRequestDialogOpened },
                notificationPermissionState = notificationPermissionState,
                alarmPermissionState = alarmPermissionState,
                onConfirmButtonPress = {
                    isClassSettingsEnabled = !isClassSettingsEnabled
                    isExamSettingsEnabled = !isExamSettingsEnabled
                    isPermissionRequestDialogOpened = !isPermissionRequestDialogOpened
                },
                onExactAlarmPermissionRequest = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        alarmPermissionLauncher.launch(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                    }
                }
            )
            PriorityPickerDialog(isOpen = isClassPriorityDialogOpened)
            EventOptionsCard(
                icon = CalendarMonth,
                title = stringResource(R.string.activity_type_classes),
                enabled = isClassSettingsEnabled,
                onSwitchChange = onClassSwitchChange,
            )
            EventOptionsCard(
                icon = ContractEdit,
                title = stringResource(id = R.string.activity_type_exams),
                enabled = isExamSettingsEnabled,
                onSwitchChange = onExamSwitchChange,
            )
        }
    }
}

fun hasExactAlarmPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.canScheduleExactAlarms()
    } else true
}