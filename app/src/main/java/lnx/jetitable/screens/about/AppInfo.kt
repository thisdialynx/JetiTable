package lnx.jetitable.screens.about

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import lnx.jetitable.BuildConfig
import lnx.jetitable.R

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