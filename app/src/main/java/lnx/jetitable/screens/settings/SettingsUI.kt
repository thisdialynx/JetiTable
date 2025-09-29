package lnx.jetitable.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.DataState
import lnx.jetitable.navigation.About
import lnx.jetitable.navigation.Notifications
import lnx.jetitable.screens.settings.cards.AccountCard
import lnx.jetitable.screens.settings.cards.SettingsCard
import lnx.jetitable.screens.settings.cards.UpdateCard
import lnx.jetitable.viewmodel.SettingsViewModel
import lnx.jetitable.viewmodel.UserDataUiState

enum class SettingItem(
    val titleResId: Int,
    val descriptionResId: Int,
    val icon: ImageVector,
    val destination: String
) {
    NOTIFICATIONS(R.string.notifications_settings_entry_title, R.string.notifications_settings_entry_description, lnx.jetitable.ui.icons.google.Notifications, Notifications.route),
    ABOUT(R.string.about_screen_title, R.string.about_screen_description, lnx.jetitable.ui.icons.google.Info, About.route)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsUI(
    onBack: () -> Unit,
    onDestinationNavigate: (String) -> Unit,
    onSignOut: () -> Unit,
    updateInfo: DataState<out SettingsViewModel.AppUpdateInfo>,
    userDataUiState: DataState<out UserDataUiState>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings)) },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AccountCard(
                    userDataUiState = userDataUiState,
                    onSignOut = onSignOut
                )
            }
            item {
                UpdateCard(updateInfo)
            }
            item {
                SettingsCard(onDestinationNavigate = onDestinationNavigate)
            }
        }
    }
}