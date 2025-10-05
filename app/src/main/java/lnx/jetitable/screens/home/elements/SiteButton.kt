package lnx.jetitable.screens.home.elements

import android.content.ClipData
import android.util.Log
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lnx.jetitable.R

@Composable
fun SiteButton(
    modifier: Modifier = Modifier,
    url: String,
    icon: ImageVector,
    color: Color,
    uriHandler: UriHandler,
    onButtonClick: () -> Unit = {},
    clipboardManager: Clipboard
) {
    val label = stringResource(R.string.meeting_url_label)

    CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = color
            ),
            modifier = modifier
                .size(40.dp)
                .combinedClickable(
                    onClick = {
                        try {
                            uriHandler.openUri(url)
                            onButtonClick()
                        } catch (e: Exception) {
                            Log.d("Site Button", "Failed to open url", e)
                        }
                    },
                    onLongClick = {
                        clipboardManager.nativeClipboard.setPrimaryClip(
                            ClipData.newPlainText(AnnotatedString(label), AnnotatedString(url))
                        )
                    }
                )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}