package lnx.jetitable.screens.about

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap

@Composable
fun AboutScreen(onBack: () -> Unit) {
    val packageManager = LocalContext.current.packageManager
    val drawable = packageManager.getApplicationIcon("lnx.jetitable")
    val appIcon = drawable.toBitmap(config = Bitmap.Config.ARGB_8888).asImageBitmap()

    AboutUI(onBack = { onBack() }) {
        Image(
            bitmap = appIcon,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    AboutUI(
        onBack = {},
        appIcon = {}
    )
}