package lnx.jetitable.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R

enum class ContributorItem(
    val profilePictureUrl: String? = null,
    val title: String,
    val description: Int,
    val icon: ImageVector,
    val iconDescription: String,
    val shortUrl: String
) {
    DIALYNX(
        profilePictureUrl = "https://github.com/thisdialynx.png",
        title = "Dialynx",
        description = R.string.developer,
        icon = lnx.jetitable.ui.icons.Telegram,
        iconDescription = "Telegram",
        shortUrl = "t.me/placeholder"
    ),
    DENYSRATOV(
        title = "Denys Ratov",
        description = R.string.timetable_developer,
        icon = lnx.jetitable.ui.icons.Telegram,
        iconDescription = "Telegram",
        shortUrl = "t.me/placeholder",
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutUI(onBack: () -> Unit, appIcon: @Composable () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val localUriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.about_screen_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = { onBack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AppInfo(localUriHandler) { appIcon() }
            }
            item {
                Text(
                    text = stringResource(id = R.string.product),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                ContributorsCard(localUriHandler)
            }
        }
    }
}