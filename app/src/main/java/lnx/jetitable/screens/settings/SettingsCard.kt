package lnx.jetitable.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SettingsCard(onDestinationNavigate: (String) -> Unit) {
    SettingItem.entries.forEachIndexed { index, item ->
        val isLastItem = index == SettingItem.entries.size - 1

        val topStart = if (index == 0) 12.dp else 4.dp
        val topEnd = if (index == 0) 12.dp else 4.dp
        val bottomStart = if (isLastItem) 12.dp else 4.dp
        val bottomEnd = if (isLastItem) 12.dp else 4.dp

        val shape = RoundedCornerShape(
            topStart = topStart,
            topEnd = topEnd,
            bottomStart = bottomStart,
            bottomEnd = bottomEnd
        )

        if (index > 0) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.surface,
                thickness = 2.dp
            )
        }

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            onClick = { onDestinationNavigate(item.destination) },
            modifier = Modifier.fillMaxWidth(),
            shape = shape
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = lnx.jetitable.ui.icons.google.Info,
                    contentDescription = "${stringResource(id = item.titleResId)}. ${stringResource(id = item.descriptionResId)}",
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(id = item.titleResId),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(id = item.descriptionResId),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}