package lnx.jetitable.screens.home.card

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SiteButton(url: String, icon: ImageVector, color: Color, uriHandler: UriHandler, clipboardManager: ClipboardManager, onClick: () -> Unit) {
    CompositionLocalProvider(value = LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = color
            ),
            modifier = Modifier
                .size(40.dp)
                .combinedClickable(
                    onClick = {
                        uriHandler.openUri(url)
                        onClick()
                    },
                    onLongClick = {
                        clipboardManager.setText(AnnotatedString(url))
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