package lnx.jetitable.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import lnx.jetitable.R
import lnx.jetitable.navigation.Home

enum class UrlIconData(val icon: ImageVector, val description: Int, val shortUri: String) {
    GITHUB(lnx.jetitable.ui.icons.Github, R.string.github, "github.com/thisdialynx/JetiTable"),
    UNIVERSITY(lnx.jetitable.ui.icons.Snu, R.string.university, "snu.edu.ua"),
    TIMETABLE(lnx.jetitable.ui.icons.google.CalendarMonth, R.string.timetable, "timetable.lond.lg.ua"),
    SUPPORT(lnx.jetitable.ui.icons.Telegram, R.string.support, "t.me/JetiTable")
}

enum class ContibutorsData(
    val profilePicture: Int,
    val title: String,
    val description: Int,
    val icon: ImageVector,
    val iconDescription: String,
    val shortUri: String
) {
    DIALYNX(0, "Dialynx", R.string.developer, lnx.jetitable.ui.icons.Telegram, "Telegram", "t.me/placeholder"),
    DENYSRATOV(0, "Denys Ratov", R.string.timetable_developer, lnx.jetitable.ui.icons.Telegram, "Telegram", "t.me/placeholder")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.about_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Home.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            val localUriHandler = LocalUriHandler.current

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppInfo()
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    UrlIconData.entries.forEach { data ->
                        UrlIconTile(
                            icon = data.icon,
                            description = data.description,
                            shortUri = data.shortUri,
                            localUriHandler = localUriHandler
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(id = R.string.product),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                ) {
                    ContibutorsData.entries.forEachIndexed { index, data ->
                        if (index > 0) {
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                        ContributorCard(
                            profilePicture = data.profilePicture,
                            title = data.title,
                            description = data.description,
                            icon = lnx.jetitable.ui.icons.Telegram,
                            iconDescription = data.iconDescription,
                            localUriHandler = localUriHandler,
                            shortUri = data.shortUri
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UrlIconTile(icon: ImageVector, description: Int, shortUri: String, localUriHandler: UriHandler) {
    Card(
        onClick = { localUriHandler.openUri("https://$shortUri") },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = description),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(id = description),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


