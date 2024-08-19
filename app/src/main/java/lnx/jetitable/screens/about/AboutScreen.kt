package lnx.jetitable.screens.about

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import lnx.jetitable.BuildConfig
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
                            icon = R.drawable.ic_github,
                            description = "Github", shortUri = "github.com/thisdialynx/JetiTable",
                            localUriHandler = localUriHandler
                        )
                        UrlIconTile(
                            icon = R.drawable.ic_captive_portal,
                            description = stringResource(id = R.string.university),
                            shortUri = "snu.edu.ua",
                            localUriHandler = localUriHandler
                        )
                        UrlIconTile(
                            icon = R.drawable.ic_calendar,
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
                    icon = R.drawable.ic_telegram,
                    iconDescription = "Telegram",
                    localUriHandler = localUriHandler,
                    shortUri = "t.me/placeholder"
                )
                ContributorCard(
                    profilePicture = 0,
                    title = "Denys Ratov",
                    description = R.string.timetable_developer,
                    icon = R.drawable.ic_telegram,
                    iconDescription = "Telegram",
                    localUriHandler = localUriHandler,
                    shortUri = "t.me/placeholder"
                )
            }
        }
    }
}

@Composable
fun AppInfo() {
    val packageManager = LocalContext.current.packageManager
    val drawable = packageManager.getApplicationIcon("lnx.jetitable")
    val version = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

    Image(
        drawable.toBitmap(config = Bitmap.Config.ARGB_8888).asImageBitmap(),
        contentDescription = "App Icon", modifier = Modifier
            .size(100.dp)
            .padding(8.dp)
    )
    Text(
        text = stringResource(id = R.string.app_name),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelMedium,
        fontSize = 20.sp
    )
    Text(
        text = version,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun UrlIconTile(icon: Int, description: String, shortUri: String, localUriHandler: UriHandler) {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp),
        ),
        onClick = { localUriHandler.openUri("https://$shortUri") }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = description,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ContributorCard(profilePicture: Int, title: String, description: Int, icon: Int, iconDescription: String, localUriHandler: UriHandler, shortUri: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp), Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = profilePicture),
                contentDescription = "$title $description",
                modifier = Modifier
                    .fillMaxWidth(0.15f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp
                )
                Text(
                    text = stringResource(id = description),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Card(
                modifier = Modifier.wrapContentWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)
                ),
                onClick = { localUriHandler.openUri("https://$shortUri") }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = iconDescription,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
