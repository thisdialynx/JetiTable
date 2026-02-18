package lnx.jetitable.screens.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import lnx.jetitable.viewmodel.NotifViewModel
import lnx.jetitable.viewmodel.SchedulePrefs

@Composable
fun NotificationsScreen(
    viewModel: NotifViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val notificationPreference by viewModel.notificationPreference.collectAsStateWithLifecycle(null)
    val schedulePrefs by viewModel.schedulePrefs.collectAsStateWithLifecycle(SchedulePrefs())

    NotificationsUI(
        onBack = onBack,
        notificationsEnabled = notificationPreference,
        onNotificationSwitchChange = {
            if (it) {
                viewModel.enableNotifications()
            } else {
                viewModel.disableNotifications()
            }
        },
        schedulePrefs = schedulePrefs,
        onExamSwitchChange = {
            if (it) {
                viewModel.enableExamNotifications()
            } else {
                viewModel.disableExamNotifications()
            }
        },
        onClassSwitchChange = {
            if (it) {
                viewModel.enableClassNotifications()
            } else {
                viewModel.disableClassNotifications()
            }
        },
        onExamTimeSelected = {
            viewModel.updateExamMinutes(minutes = it)
        },
        onClassTimeSelected = {
            viewModel.updateClassMinutes(minutes = it)
        }
    )
}

@Preview
@Composable
private fun Preview() {
    NotificationsUI(
        onBack = {},
        notificationsEnabled = false,
        onNotificationSwitchChange = {},
        schedulePrefs = SchedulePrefs(),
        onExamSwitchChange = {},
        onClassSwitchChange = {},
        onClassTimeSelected = {},
        onExamTimeSelected = {},
    )
}