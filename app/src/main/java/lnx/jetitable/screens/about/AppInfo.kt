package lnx.jetitable.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lnx.jetitable.BuildConfig
import lnx.jetitable.R

@Composable
fun AppInfo(localUriHandler: UriHandler, appIcon: @Composable () -> Unit) {
    val version = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

    Spacer(modifier = Modifier.height(8.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        appIcon()

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

        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            UrlIconTiles(
                localUriHandler = localUriHandler,
            )
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
}