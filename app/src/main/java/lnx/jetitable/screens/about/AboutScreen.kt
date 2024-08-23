package lnx.jetitable.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import lnx.jetitable.R
import lnx.jetitable.navigation.Home

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.about_screen_title)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Home.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val localUriHandler = LocalUriHandler.current

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                AppInfo()
                Spacer(modifier = Modifier.padding(8.dp))
                Card(
                    modifier = Modifier.wrapContentWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                    )
                ) {
                    Row {
                        UrlIconTile(
                            icon = lnx.jetitable.ui.icons.Github,
                            description = "Github", shortUri = "github.com/thisdialynx/JetiTable",
                            localUriHandler = localUriHandler
                        )
                        UrlIconTile(
                            icon = lnx.jetitable.ui.icons.Snu,
                            description = stringResource(id = R.string.university),
                            shortUri = "snu.edu.ua",
                            localUriHandler = localUriHandler
                        )
                        UrlIconTile(
                            icon = lnx.jetitable.ui.icons.google.CalendarMonth,
                            description = "TimeTable",
                            shortUri = "timetable.lond.lg.ua",
                            localUriHandler = localUriHandler
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.padding(4.dp))
                ContributorCard(
                    profilePicture = 0,
                    title = "Dialynx",
                    description = R.string.developer,
                    icon = lnx.jetitable.ui.icons.Telegram,
                    iconDescription = "Telegram",
                    localUriHandler = localUriHandler,
                    shortUri = "t.me/placeholder"
                )
                ContributorCard(
                    profilePicture = 0,
                    title = "Denys Ratov",
                    description = R.string.timetable_developer,
                    icon = lnx.jetitable.ui.icons.Telegram,
                    iconDescription = "Telegram",
                    localUriHandler = localUriHandler,
                    shortUri = "t.me/placeholder"
                )
            }
        }
    }
}


