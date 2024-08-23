package lnx.jetitable.screens.settings

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import lnx.jetitable.R
import lnx.jetitable.misc.getSemester
import lnx.jetitable.navigation.Home

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Home.route) }) {
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
        val calendar = Calendar.getInstance()

        val userId = settingsViewModel.userId
        val fullName = settingsViewModel.fullName
        val fullNameId = settingsViewModel.fullNameId
        val group = settingsViewModel.group
        val groupId = settingsViewModel.groupId
        val isFullTime = if (settingsViewModel.isFullTime == true) R.string.full_time else R.string.part_time
        val semester = if (getSemester(calendar.get(Calendar.MONTH) + 1) == "1") R.string.autumn_semester else R.string.spring_semester

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TTAccountCard(
                title = stringResource(id = R.string.timetable_account, stringResource(id = R.string.timetable)),
                description = "$fullName ($group). \n${stringResource(id = semester)}. ${stringResource(id = isFullTime)}.",
                icon = lnx.jetitable.ui.icons.Snu,
                context = context,
                toastText = "UID: $userId, GID: $groupId, FNID: $fullNameId."
            )
        }
    }
}

