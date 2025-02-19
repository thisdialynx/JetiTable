package lnx.jetitable.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import lnx.jetitable.R
import lnx.jetitable.misc.DateManager
import lnx.jetitable.navigation.About
import lnx.jetitable.viewmodel.SettingsViewModel

enum class Setting(
    val titleResId: Int,
    val descriptionResId: Int,
    val icon: ImageVector,
    val destination: String
) {
    ABOUT(R.string.about_screen_title, R.string.about_screen_description, lnx.jetitable.ui.icons.google.Info, About.route)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back arrow"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val settingsViewModel: SettingsViewModel = viewModel()
        val context = LocalContext.current
        val dateManager = DateManager()

        val userId = settingsViewModel.userId
        val fullName = settingsViewModel.fullName
        val fullNameId = settingsViewModel.fullNameId
        val group = settingsViewModel.group
        val groupId = settingsViewModel.groupId
        val isFullTime = if (settingsViewModel.isFullTime == true) R.string.full_time else R.string.part_time
        val semester = if (dateManager.getSemester() == 1) R.string.autumn_semester else R.string.spring_semester
        val academicYear = settingsViewModel.academicYear
        val status = settingsViewModel.status

        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TTAccountCard(
                title = stringResource(id = R.string.timetable_account, stringResource(id = R.string.timetable)),
                description = "$fullName ($group). \n${stringResource(id = R.string.status)}: $status. $academicYear. ${stringResource(id = semester)}. ${stringResource(id = isFullTime)}.",
                icon = lnx.jetitable.ui.icons.Snu,
                context = context,
                toastText = "UID: $userId, GID: $groupId, FNID: $fullNameId.",
                settingsViewModel = settingsViewModel
            )
            Spacer(modifier = Modifier.height(2.dp))
            SettingEntries(navController)
        }
    }
}

@Composable
fun SettingEntries(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Setting.entries.forEachIndexed { index, settingEntry ->
            if (index > 0) {
                HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.surface)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = { navController.navigate(settingEntry.destination) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = lnx.jetitable.ui.icons.google.Info,
                        contentDescription = "${stringResource(id = settingEntry.titleResId)}. ${stringResource(id = settingEntry.descriptionResId)}",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(id = settingEntry.titleResId),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = stringResource(id = settingEntry.descriptionResId),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}