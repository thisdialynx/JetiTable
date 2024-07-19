package lnx.jetitable.screens.about

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val localUriHandler = LocalUriHandler.current

            AppInfo()
            Spacer(modifier = Modifier.padding(8.dp))
            Card(
                modifier = Modifier
                    .wrapContentWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                )
            ) {
                Row {
                    UrlIconCard(icon = R.drawable.ic_github, description = "Github", shortUri = "github.com/thisdialynx/JetiTable", localUriHandler = localUriHandler)
                    UrlIconCard(icon = R.drawable.ic_captive_portal, description = "University", shortUri = "snu.edu.ua", localUriHandler = localUriHandler)
                    UrlIconCard(icon = R.drawable.ic_calendar, description = "TimeTable", shortUri = "timetable.lond.lg.ua", localUriHandler = localUriHandler)
                }
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
        contentDescription = "Image", modifier = Modifier
            .size(100.dp)
            .padding(8.dp)

    )
    Text(
        text = stringResource(id = R.string.app_name),
        color = MaterialTheme.colorScheme.primary,
        fontSize = 20.sp
    )
    Text(
        text = version,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun UrlIconCard(icon: Int, description: String, shortUri: String, localUriHandler: UriHandler) {
    Card(
        modifier = Modifier
            .clickable { localUriHandler.openUri("https://$shortUri") }
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                )
            Text(text = description)
        }
    }
}
