package lnx.jetitable.screens.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import lnx.jetitable.viewmodel.NotifViewModel

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    val viewModel = viewModel<NotifViewModel>()
    val notificationPreference = viewModel.notificationPreference.collectAsStateWithLifecycle(false)

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
        }
    )
}

@Preview
@Composable
private fun Preview() {
    val fakeState = remember { mutableStateOf(false) }
    NotificationsUI(
        onBack = {},
        onNotificationSwitchChange = {},
        notificationsEnabled = fakeState,
        onExamSwitchChange = {},
        onClassSwitchChange = {}
    )
}