package lnx.jetitable.features.about.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.features.about.presentation.ProjectLinkUi
import lnx.jetitable.ui.components.getRowCardShape

@Composable
fun UrlIconTiles(localUriHandler: UriHandler, links: List<ProjectLinkUi>) {
    links.forEachIndexed { index, entry ->
        val isLast = index == links.size - 1
        val shape = getRowCardShape(index, isLast)

        Surface(
            onClick = {
                localUriHandler.openUri("https://${entry.url}")
            },
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
            shape = shape
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = entry.platformIcon,
                    contentDescription = stringResource(id = entry.titleResId),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(id = entry.titleResId),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}