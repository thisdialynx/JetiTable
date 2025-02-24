package lnx.jetitable.screens.about

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import lnx.jetitable.BuildConfig
import lnx.jetitable.R

@Composable
fun AppInfo(localUriHandler: UriHandler) {
    val packageManager = LocalContext.current.packageManager
    val drawable = packageManager.getApplicationIcon("lnx.jetitable")
    val version = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

    Spacer(modifier = Modifier.height(8.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
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

        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            UrlIconItem.entries.forEach { data ->
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
}