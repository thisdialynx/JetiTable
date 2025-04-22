package lnx.jetitable.screens.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.screens.auth.UrlIconItem

@Composable
fun UrlIconTiles(localUriHandler: UriHandler) {
    UrlIconItem.entries.forEachIndexed { index, entry ->
        val isFirstEntry = index == 0
        val isLastEntry = index == UrlIconItem.entries.size - 1
        val isMiddleEntry = index > 0 && index < UrlIconItem.entries.size - 1

        val topStart = if (isFirstEntry) 12.dp else 4.dp
        val topEnd = if (isFirstEntry) 4.dp else 12.dp
        val bottomStart = if (isLastEntry) 4.dp else 12.dp
        val bottomEnd = if (isLastEntry) 12.dp else 4.dp

        val shape = if (isMiddleEntry) {
            RoundedCornerShape(4.dp, 4.dp, 4.dp, 4.dp)
        } else {
            RoundedCornerShape(
                topStart = topStart,
                topEnd = topEnd,
                bottomStart = bottomStart,
                bottomEnd = bottomEnd
            )
        }

        Surface(
            onClick = {
                localUriHandler.openUri("https://${entry.shortUri}")
            },
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = shape
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = entry.icon,
                    contentDescription = stringResource(id = entry.description),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(id = entry.description),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}