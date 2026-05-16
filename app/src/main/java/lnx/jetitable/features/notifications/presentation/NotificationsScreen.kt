package lnx.jetitable.features.notifications.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationsScreen(
    viewModel: NotifViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle(NotificationsScreenState())
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        viewModel.updatePermissionStatuses(hasNotif = viewModel.hasPostNotificationPermission())
    }
    val exactAlarmPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        val hasAlarm = viewModel.hasExactAlarmPermission()
        viewModel.updatePermissionStatuses(hasAlarm = hasAlarm)
    }

    NotificationLayout(
        onBack = onBack,
        onNotificationSwitchChange = viewModel::onToggleAppNotifications,
        onExamSwitchToggle = viewModel::onExamNotificationsToggle,
        onClassSwitchToggle = viewModel::onClassNotificationsToggle,
        onExamTimeSelected = viewModel::updateExamMinutes,
        onClassTimeSelected = viewModel::updateClassMinutes,
        onNotificationPermissionRequest = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        },
        onDialogVisibilityToggle = viewModel::onPermissionDialogToggle,
        screenState = screenState,
        onExactAlarmPermissionRequest = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                exactAlarmPermissionLauncher.launch(intent)
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    NotificationLayout(
        screenState = NotificationsScreenState(),
        onClassSwitchToggle = {},
        onExamSwitchToggle = {},
        onDialogVisibilityToggle = {},
        onNotificationPermissionRequest = {},
        onExactAlarmPermissionRequest = {},
        onBack = {},
        onNotificationSwitchChange = {},
        onClassTimeSelected = {},
        onExamTimeSelected = {}
    )
}