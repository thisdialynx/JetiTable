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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import lnx.jetitable.R
import lnx.jetitable.screens.notifications.dialogs.PermissionRequestDialog
import lnx.jetitable.ui.components.AppSnackbar
import lnx.jetitable.ui.icons.google.CalendarMonth
import lnx.jetitable.ui.icons.google.ContractEdit

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun NotificationsUI(
    onBack: () -> Unit,
    isNotificationsEnabled: State<Boolean>,
    onClassSwitchChange: (Boolean) -> Unit,
    onExamSwitchChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val isClassPriorityDialogOpened = remember { mutableStateOf(false) }
    val notifSwitch = remember { mutableStateOf(isNotificationsEnabled.value) }
    val isPermissionRequestDialogOpened = remember { mutableStateOf(false) }
    val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    val alarmPermissionState = remember { mutableStateOf(false) }
    val isClassSettingsEnabled = remember { mutableStateOf(false) }
    val isExamSettingsEnabled = remember { mutableStateOf(false) }
    val alarmPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult())
            { alarmPermissionState.value = hasExactAlarmPermission(context) }

    LaunchedEffect(Unit) {
        alarmPermissionState.value = hasExactAlarmPermission(context)
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
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.notifications_screen_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
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
                            checked = notifSwitch.value,
                            onCheckedChange = {
                                if (it && !alarmPermissionState.value && !notificationPermissionState.status.isGranted) {
                                    isPermissionRequestDialogOpened.value = !isPermissionRequestDialogOpened.value
                                }
                            }
                        )
                    }
                }

                PermissionRequestDialog(
                    isDialogOpened = isPermissionRequestDialogOpened,
                    isNotificationsEnabled = notifSwitch,
                    notificationPermissionState = notificationPermissionState,
                    alarmPermissionState = alarmPermissionState,
                    onConfirmButtonPress = {
                        isClassSettingsEnabled.value = !isClassSettingsEnabled.value
                        isExamSettingsEnabled.value = !isExamSettingsEnabled.value

                        isPermissionRequestDialogOpened.value = !isPermissionRequestDialogOpened.value
                    },
                    onExactAlarmPermissionRequest = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            alarmPermissionLauncher.launch(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                        }
                    }
                )
            }
            item {
                PriorityPickerDialog(isOpen = isClassPriorityDialogOpened)
                EventOptionsCard(
                    icon = CalendarMonth,
                    title = stringResource(R.string.activity_type_classes),
                    enabled = isClassSettingsEnabled.value,
                    onSwitchChange = onClassSwitchChange,
                )
            }
            item {
                EventOptionsCard(
                    icon = ContractEdit,
                    title = stringResource(id = R.string.activity_type_exams),
                    enabled = isExamSettingsEnabled.value,
                    onSwitchChange = onExamSwitchChange,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityPickerDialog(
    isOpen: MutableState<Boolean>
) {
    if (isOpen.value) {
        BasicAlertDialog(
            onDismissRequest = { isOpen.value = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row {
                        Text(
                            text = "High",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = true,
                            onClick = null
                        )
                    }
                    Row {
                        Text(
                            text = "Medium",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = true,
                            onClick = null
                        )
                    }
                    Row {
                        Text(
                            text = "Low",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        RadioButton(
                            selected = true,
                            onClick = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventOptionsCard(
    icon: ImageVector,
    title: String,
    enabled: Boolean,
    onSwitchChange: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = !checked
                        onSwitchChange(it)
                    },
                    enabled = enabled
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12, 12, 4, 4)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Priority",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = "High",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(4, 4, 12, 12)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Minutes",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = "15",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun hasExactAlarmPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.canScheduleExactAlarms()
    } else true
}