package lnx.jetitable.screens.settings

import android.content.Context
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import lnx.jetitable.BuildConfig
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
                icon = R.drawable.ic_snu,
                context = context,
                toastText = "UID: $userId, GID: $groupId, FNID: $fullNameId."
            )
        }
    }
}

@Composable
fun TTAccountCard(title: String, description: String, icon: Int, context: Context, toastText: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = {
            if (BuildConfig.DEBUG) {
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT ).show()
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "$title $description",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Log out", modifier = Modifier.padding(start = 4.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}